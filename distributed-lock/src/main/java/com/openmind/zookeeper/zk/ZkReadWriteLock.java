package com.openmind.zookeeper.zk;

import com.openmind.zookeeper.DistributedLock;
import com.openmind.zookeeper.LockStatus;
import com.openmind.zookeeper.ReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-11
 * @time 11:38
 * @desc
 */
@Slf4j
public class ZkReadWriteLock implements ReadWriteLock {
    private final static String SHARE_LOCK_PATH = "/share_locks";
    /**
     * 自旋测试超时阈值，考虑到网络的延时性，这里设为1000毫秒
     */
    private static final long spinForTimeoutThreshold = 1000L;
    private static final long SLEEP_TIME = 100L;
    private static final int SESSION_TIMEOUT = 1000;
    private static final String READ_LOCK_FLAG = "-read-";
    private static final String WRITE_LOCK_FLAG = "-write-";

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private ZooKeeper zooKeeper;
    private Comparator<String> tempSeqNodeComparator;


    private ReadLock readLock = new ReadLock();
    private WriteLock writeLock = new WriteLock();

    /**
     * 创建度缩写实例
     *
     * @param config zkpath路径
     * @throws IOException
     * @throws InterruptedException
     */
    public ZkReadWriteLock(String config) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(config, SESSION_TIMEOUT, new LockNodeWatcher());
        connectedSemaphore.await();

        tempSeqNodeComparator = (x, y) -> {
            Integer xs = getSequence(x);
            Integer ys = getSequence(y);
            return xs > ys ? 1 : (xs < ys ? -1 : 0);
        };
    }

    @Override
    public DistributedLock readLock() {
        return readLock;
    }

    @Override
    public DistributedLock writeLock() {
        return writeLock;
    }


    class ReadLock implements DistributedLock, Watcher {
        private LockStatus lockStatus = LockStatus.UNLOCK;
        private CyclicBarrier lockBarrier = new CyclicBarrier(2);
        private String prefix = new Random(System.nanoTime()).nextInt(10000000) + READ_LOCK_FLAG;
        private String name;

        @Override
        public void lock() throws Exception {
            if (lockStatus != LockStatus.UNLOCK) {
                return;
            }

            // 1. 创建锁节点
            if (name == null) {
                name = createLockNode(prefix);
                name = name != null ? name.substring(name.lastIndexOf("/") + 1) : null;
                log.info("创建锁节点：{}", name);
            }


            // 2. 获取锁节点列表。设置监视器
            List<String> subNodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
            subNodes = sortLockNodes(subNodes);

            // 3.检查能否获取锁，若能，直接返回
            if (canAcquireLock(name, subNodes)) {
                log.info(name + " 获取锁");
                lockStatus = LockStatus.LOCKED;
                return;
            }

            lockStatus = LockStatus.TRY_LOCK;
            lockBarrier.await();
        }

        @Override
        public boolean tryLock() throws Exception {
            if (lockStatus == LockStatus.LOCKED) {
                return true;
            }

            if (name == null) {
                name = createLockNode(prefix);
                name = name != null ? name.substring(name.lastIndexOf("/") + 1) : null;
                log.info("{}锁节点创建成功", name);
            }

            List<String> subNodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
            subNodes = sortLockNodes(subNodes);

            if (canAcquireLock(name, subNodes)) {
                lockStatus = LockStatus.LOCKED;
                log.info("{}所节点创建成功");
                return true;
            }
            return false;
        }

        @Override
        public boolean tryLock(long millisecond) throws Exception {
            long millisTimeout = millisecond;
            if (millisTimeout <= 0L) {
                return false;
            }

            final long deadline = System.currentTimeMillis() + millisTimeout;

            for (; ; ) {
                if (tryLock()) {
                    return true;
                }

                if (millisTimeout > spinForTimeoutThreshold) {
                    Thread.sleep(SLEEP_TIME);
                }

                millisTimeout = deadline - System.currentTimeMillis();
                if (millisTimeout <= 0L) {
                    return false;
                }
            }
        }

        @Override
        public void unlock() throws Exception {
            if (lockStatus == LockStatus.UNLOCK) {
                return;
            }
            deleteLockNode(name);
            lockStatus = LockStatus.UNLOCK;
            lockBarrier.reset();
            log.info(name + " 释放锁");
            name = null;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                return;
            }
            if (Event.EventType.None == watchedEvent.getType() && watchedEvent.getPath() == null) {
                connectedSemaphore.countDown();
            } else if (Event.EventType.NodeChildrenChanged == watchedEvent.getType() &&
                    SHARE_LOCK_PATH.equals(watchedEvent.getPath())) {
                // 对于等待加锁的现成执行下面的监听逻辑
                if (lockStatus != LockStatus.TRY_LOCK) {
                    return;
                }

                List<String> subNodes = null;
                try {
                    subNodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
                    subNodes = sortLockNodes(subNodes);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                if (canAcquireLock(name, subNodes)) {
                    lockStatus = LockStatus.LOCKED;
                    try {
                        lockBarrier.await();
                        log.info(name + " 获取锁");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    class WriteLock implements DistributedLock, Watcher {
        private LockStatus lockStatus = LockStatus.UNLOCK;
        private CyclicBarrier lockBarrier = new CyclicBarrier(2);
        private String prefix = new Random(System.nanoTime()).nextInt(1000000) + WRITE_LOCK_FLAG;
        private String name;

        @Override
        public void lock() throws Exception {
            if (lockStatus != LockStatus.UNLOCK) {
                return;
            }

            if (name == null) {
                name = createLockNode(prefix);
                name = name.substring(name.lastIndexOf("/") + 1);
                log.info("创建锁节点 " + name);
            }

            List<String> subNodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
            subNodes = sortLockNodes(subNodes);

            if (isFirstNode(name, subNodes)) {
                System.out.println(name + " 获取锁");
                lockStatus = LockStatus.LOCKED;
                return;
            }

            lockStatus = LockStatus.TRY_LOCK;
            lockBarrier.await();


        }

        @Override
        public boolean tryLock() throws Exception {
            if (lockStatus == LockStatus.LOCKED) {
                return true;
            }

            if (name == null) {
                name = createLockNode(prefix);
                name = name.substring(name.lastIndexOf("/") + 1);
                System.out.println("创建锁节点 " + name);
            }

            List<String> nodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
            nodes = sortLockNodes(nodes);

            if (isFirstNode(name, nodes)) {
                lockStatus = LockStatus.LOCKED;
                return true;
            }

            return false;
        }

        @Override
        public boolean tryLock(long millisecond) throws Exception {
            long millisTimeout = millisecond;
            if (millisTimeout <= 0L) {
                return false;
            }

            final long deadline = System.currentTimeMillis() + millisTimeout;
            for (;;) {
                if (tryLock()) {
                    return true;
                }

                if (millisTimeout > spinForTimeoutThreshold) {
                    Thread.sleep(SLEEP_TIME);
                }

                millisTimeout = deadline - System.currentTimeMillis();
                if (millisTimeout <= 0L) {
                    return false;
                }
            }
        }

        @Override
        public void unlock() throws Exception {
            if (lockStatus == LockStatus.UNLOCK) {
                return;
            }

            deleteLockNode(name);
            lockStatus = LockStatus.UNLOCK;
            lockBarrier.reset();
            System.out.println(name + " 释放锁");
            name = null;
        }

        @Override
        public void process(WatchedEvent event) {
            if (Event.KeeperState.SyncConnected != event.getState()) {
                return;
            }

            if (Event.EventType.None == event.getType() && event.getPath() == null) {
                connectedSemaphore.countDown();
            } else if (Event.EventType.NodeChildrenChanged == event.getType()
                    && event.getPath().equals(SHARE_LOCK_PATH)) {

                if (lockStatus != LockStatus.TRY_LOCK) {
                    return;
                }

                List<String> subNodes = null;
                try {
                    // 获取锁列表
                    subNodes = zooKeeper.getChildren(SHARE_LOCK_PATH, this);
                    subNodes = sortLockNodes(subNodes);

                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                // 判断前面是否有写操作
                if (isFirstNode(name, subNodes)) {
                    lockStatus = LockStatus.LOCKED;
                    try {
                        lockBarrier.await();
                        System.out.println(name + " 获取锁");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class LockNodeWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (Objects.equals(Event.KeeperState.SyncConnected, event.getState())) {
                connectedSemaphore.countDown();
            }
        }
    }

    /**
     * 创建节点
     *
     * @param name
     * @return
     */
    private String createLockNode(String name) {
        String path = null;
        try {
            path = zooKeeper.create(SHARE_LOCK_PATH + "/" + name, "".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException | InterruptedException e) {
            System.out.println(" failed to create lock node");
            return null;
        }

        return path;
    }

    private void deleteLockNode(String name) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(SHARE_LOCK_PATH + "/" + name, false);
        zooKeeper.delete(SHARE_LOCK_PATH + "/" + name, stat.getVersion());
    }

    private Integer getSequence(String name) {
        return Integer.valueOf(name.substring(name.lastIndexOf("-") + 1));
    }

    /**
     * 能否请求到锁，如果第一个节点就是当前name节点，直接返回true
     * 如果节点名称在node列表中，且可读，则返回true，否则返回false
     * @param name 锁名称
     * @param nodes 节点列表
     * @return
     */
    private Boolean canAcquireLock(String name, List<String> nodes) {
        if (isFirstNode(name, nodes)) {
            return true;
        }

        Map<String, Boolean> map = new HashMap<>();
        boolean hasWriteOperation = false;
        for (String n : nodes) {
            if (n.contains("read") && !hasWriteOperation) {
                map.put(n, true);
            } else {
                hasWriteOperation = true;
                map.put((n), false);
            }
        }

        return map.get(name);
    }

    /**
     * 是否可写，第一个节点就是当前最小节点
     * @param name
     * @param nodes
     * @return
     */
    private Boolean isFirstNode(String name, List<String> nodes) {
        return nodes.get(0).equals(name);
    }

    /**
     * 过滤排序，防止在当前节点下面有不符合临时有序节点的其他节点
     * @param nodes
     * @return
     */
    private List<String> sortLockNodes(List<String> nodes) {
        if(CollectionUtils.isNotEmpty(nodes) && nodes.size() > 1) {
            nodes = nodes.parallelStream()
                    .filter(x-> x.contains(READ_LOCK_FLAG) || x.contains(WRITE_LOCK_FLAG))
                    .collect(Collectors.toList());
            nodes.sort(tempSeqNodeComparator);
        }
        return nodes;
    }
}
