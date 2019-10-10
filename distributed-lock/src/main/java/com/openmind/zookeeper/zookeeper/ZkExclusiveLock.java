package com.openmind.zookeeper.zookeeper;

import com.openmind.zookeeper.DistributedLock;
import com.openmind.zookeeper.LockStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 用ZooKeeper原生API实现分布式独享锁
 *
 * @author zhoujunwen
 * @date 2019-10-10
 * @time 11:13
 * @desc
 */
@Slf4j
public class ZkExclusiveLock implements DistributedLock {
    private static final String LOCK_NODE_PATH = "/exclusive_locks/lock";
    /**
     * 自旋测试超时阈值，考虑到网络的延时性，这里设为1000毫秒
     */
    private static final long spinForTimeoutThreshold = 1000L;
    private static final long SLEEP_TIME = 100L;
    private static final int SESSION_TIMEOUT = 1000;
    private ZooKeeper zooKeeper;
    private LockStatus lockStatus;
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private CyclicBarrier lockBarrier = new CyclicBarrier(2);

    private String id = String.valueOf(new Random(System.nanoTime()).nextInt(10000000));

    public ZkExclusiveLock(String config) throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper(config, SESSION_TIMEOUT, new LockNodeWatcher());
        lockStatus = LockStatus.UNLOCK;
        // 等待zk连接，监听器中监听到连接事件，countdown()执行，放开闭锁门闸
        connectedSemaphore.await();
        log.info("ZkExclusiveLock构造器执行完成");
    }

    @Override
    public void lock() throws Exception {
        if (lockStatus != LockStatus.UNLOCK) {
            return;
        }

        // 1.创建锁节点
        if (createLockNode()) {
            log.info("[{}]顺利地获取到锁", id);
            lockStatus = LockStatus.LOCKED;
            return;
        }

        lockStatus = LockStatus.TRY_LOCK;
        lockBarrier.await();
        log.info("[{}]惨烈地获取到锁", id);
    }

    @Override
    public boolean tryLock() {
        if (lockStatus == LockStatus.LOCKED) return true;
        Boolean created = createLockNode();
        lockStatus = created ? LockStatus.LOCKED : LockStatus.UNLOCK;
        return created;
    }

    @Override
    public boolean tryLock(long millisecond) throws Exception {
        long milliTimeout = millisecond;
        if (milliTimeout <= 0) {
            return false;
        }

        final long deadline = System.currentTimeMillis() + milliTimeout;
        for (;;) {
            if (!tryLock()) {
                if (milliTimeout > spinForTimeoutThreshold) {
                    TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                }

                milliTimeout = deadline - System.currentTimeMillis();
                if (milliTimeout <= 0) {
                    return false;
                }
            } else {
                return true;
            }

        }
    }

    @Override
    public void unlock() {
        if (lockStatus == LockStatus.UNLOCK) {
            return;
        }

        deleteLockNode();
        lockStatus = LockStatus.UNLOCK;
        lockBarrier.reset();
    }

    /**
     * 创建锁节点
     *
     * @return
     */
    private Boolean createLockNode() {
        try {
            zooKeeper.create(LOCK_NODE_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            log.info("创建锁节点成功：[{}]", id);
        } catch (KeeperException | InterruptedException e) {
            log.warn("创建锁节点失败：[{}] {}", id, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除锁
     */
    private void deleteLockNode() {
        try {
            Stat stat = zooKeeper.exists(LOCK_NODE_PATH, false);
            zooKeeper.delete(LOCK_NODE_PATH, stat.getVersion());
            zooKeeper.close();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现ZK监听器
     */
    class LockNodeWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            if (Event.KeeperState.SyncConnected != watchedEvent.getState()) {
                return;
            }

            try {
                Stat stat = zooKeeper.exists(LOCK_NODE_PATH, this);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }

            switch (watchedEvent.getType()) {
                case None:
                    if (watchedEvent.getPath() == null) {
                        connectedSemaphore.countDown();
                    }
                    break;
                case NodeDeleted:
                    if (watchedEvent.getPath() != null && watchedEvent.getPath().equals(LOCK_NODE_PATH)) {
                        if (lockStatus == LockStatus.TRY_LOCK && createLockNode()) {
                            lockStatus = LockStatus.LOCKED;
                            try {
                                lockBarrier.await();
                                log.info("LockNodeWatcher [" + id + "]" + " 获取锁");
                                return;
                            } catch (InterruptedException | BrokenBarrierException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            }
        }
    }
}
