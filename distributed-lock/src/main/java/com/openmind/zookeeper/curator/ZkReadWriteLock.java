package com.openmind.zookeeper.curator;

import com.openmind.zookeeper.DistributedLock;
import com.openmind.zookeeper.LockStatus;
import com.openmind.zookeeper.ReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 读写共享锁
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 15:17
 * @desc
 */
@Slf4j
public class ZkReadWriteLock implements ReadWriteLock {
    private static final String ROOT_SHARE_LOCK = "/share_locks/lock";
    /**
     * 自旋测试超时阈值，考虑到网络的延时性，这里设为1000毫秒
     */
    private static final long spinForTimeoutThreshold = 1000L;
    private static final long SLEEP_TIME = 100L;
    private static final String READ_LOCK_FLAG = "read";
    private CuratorFramework curatorFramework;

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private ReadLock readLock;
    private WriteLock writeLock;
    private LockStatus lockStatus;

    /**
     * @param config zookeerper服务地址，格式为：127.0.0.1:2181,127.0.0.2:2181
     * @throws InterruptedException
     */
    public ZkReadWriteLock(String config) throws InterruptedException {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(config)
                // 超时会话时间 100000ms
                .sessionTimeoutMs(100000)
                // 连接超时时间 1500 ms
                .connectionTimeoutMs(1500)
                // 客户端重试策略, 初始化休眠时间为100ms，最大重试次数为10，最大休眠时间为5000ms
                .retryPolicy(new ExponentialBackoffRetry(100, 10, 5000))
                .build();
        curatorFramework.start();
        curatorFramework.blockUntilConnected();
    }

    @Override
    public DistributedLock readLock() {
        return new ReadLock(this);
    }

    @Override
    public DistributedLock writeLock() {
        return writeLock;
    }


    protected class ReadLock implements DistributedLock {
        private LockStatus lockStatus = LockStatus.UNLOCK;

        private CyclicBarrier lockBarrier = new CyclicBarrier(2);

        private String prefix = new Random(System.nanoTime()).nextInt(10000000) + "-" + READ_LOCK_FLAG + "-";

        private String name;

        ReadLock(ReadWriteLock lock) {
            try {
                pathChilderListener(ROOT_SHARE_LOCK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取读锁
         *
         * @throws Exception
         */
        @Override
        public void lock() throws Exception {
            if (lockStatus != LockStatus.UNLOCK) {
                return;
            }

            if (!tryLock()) {
                lockStatus = LockStatus.TRY_LOCK;
                lockBarrier.await();
            }
        }

        @Override
        public boolean tryLock() throws Exception {
            if (lockStatus == LockStatus.LOCKED) {
                return true;
            }

            // 1.创建临时有序锁节点
            if (name == null) {
                name = createLockNode(prefix);
                name = name != null ? name.substring(name.lastIndexOf("/") + 1) : "null";
                log.info("创建读锁 {}", name);
            }

            // 2.获取锁节点列表，并设置监视器
            List<String> nodes = curatorFramework.getChildren().watched().forPath(ROOT_SHARE_LOCK);
            nodes.sort(String::compareTo);

            // 3. 检查能否获取锁，若能，直接返回
            // 属于公平锁
            Boolean readable = canAcquireLock(name, nodes);
            if (readable != null && readable) {
                log.info("锁：{} 获取到锁", name);
                lockStatus = LockStatus.LOCKED;
                return true;
            }

            return false;
        }

        @Override
        public boolean tryLock(long millisecond) throws Exception {
            long milldTimeout = millisecond;
            if (milldTimeout <= 0L) {
                return false;
            }

            final long deadline = System.currentTimeMillis() + milldTimeout;
            for (; ; ) {
                if (tryLock()) {
                    return true;
                }

                if (milldTimeout > spinForTimeoutThreshold) {
                    Thread.sleep(SLEEP_TIME);
                }

                milldTimeout = deadline - System.currentTimeMillis();
                if (milldTimeout <= 0L) {
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
            log.info("释放锁：{}", name);
            name = null;
        }

        private void pathChilderListener(String path) throws Exception {
            final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
            pathChildrenCache.getListenable().addListener((client, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        log.info("添加节点 path= {}, data = {}", event.getData().getPath(),
                                new String(event.getData().getData(), StandardCharsets.UTF_8));
                        break;
                    case CHILD_UPDATED:
                    case CHILD_REMOVED:
                        if (event.getData().getPath().equals(ROOT_SHARE_LOCK)) {
                            if (lockStatus != LockStatus.TRY_LOCK) {
                                return;
                            }

                            List<String> nodes = curatorFramework.getChildren().watched().forPath(ROOT_SHARE_LOCK);
                            nodes.sort(String::compareTo);

                            Boolean readable = canAcquireLock(name, nodes);
                            if (readable != null && readable) {
                                lockStatus = LockStatus.LOCKED;
                                log.info("锁：{} 获取到锁", name);
                            }

                        }
                        break;
                }
            });
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        }
    }

    protected class WriteLock implements DistributedLock {

        @Override
        public void lock() throws Exception {

        }

        @Override
        public boolean tryLock() throws Exception {
            return false;
        }

        @Override
        public boolean tryLock(long millisecond) throws Exception {
            return false;
        }

        @Override
        public void unlock() throws Exception {

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

        String nodePath = ROOT_SHARE_LOCK + "/" + name;
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodePath);
            if (stat != null) {
//                curatorFramework.setData().forPath(nodePath, "".getBytes());
                path = nodePath;
            } else {
                path = curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(nodePath, "".getBytes());
            }
        } catch (Exception e) {
            return null;
        }
        log.info("创建节点返回的路径：{}", path);
        return path;
    }

    /**
     * 删除节点
     *
     * @param name
     * @throws Exception
     */
    private void deleteLockNode(String name) throws Exception {
        String nodePath = ROOT_SHARE_LOCK + "/" + name;
        Stat stat = curatorFramework.checkExists().forPath(nodePath);
        if (stat != null) {
            curatorFramework.delete()
                    .guaranteed()
                    .deletingChildrenIfNeeded()
                    .forPath(nodePath);
        }
    }

    /**
     * 能否获取到锁
     *
     * @param name  所名称
     * @param nodes 锁节点
     * @return
     */
    private Boolean canAcquireLock(String name, List<String> nodes) {
        if (isFirstNode(name, nodes)) {
            return true;
        }

        Map<String, Boolean> map = new HashMap<>();
        boolean hasWriteOperator = false;
        for (String n : nodes) {
            if (n.contains(READ_LOCK_FLAG) && !hasWriteOperator) {
                map.put(n, true);
            } else {
                hasWriteOperator = true;
                map.put(n, false);
            }
        }
        return map.get(name);
    }

    private boolean isFirstNode(String name, List<String> nodes) {
        return Objects.equals(nodes.get(0), name);
    }

    public void nodeListener(String path) throws Exception {
        final NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
        nodeCache.getListenable().addListener(
                () -> {
                    if (nodeCache.getCurrentData() != null) {
                        System.out.println("\n .nodeCache------节点数据发生了改变，发生的路径为："
                                + nodeCache.getCurrentData().getPath() + ",节点数据发生了改变 ，新的数据为："
                                + new String(nodeCache.getCurrentData().getData()) + "\n");
                    }
                }
        );
        nodeCache.start(true);
    }

    public void close() {
        CloseableUtils.closeQuietly(curatorFramework);
    }

    /**
     * 注册PathChildrenCache监听器
     *
     * @throws Exception
     */
    private void registerWatcher() throws Exception {
        /*
         * 注册监听器，当前节点不存在，创建该节点：未抛出异常及错误日志
         *  注册子节点触发type=[CHILD_ADDED]
         *  更新触发type=[CHILD_UPDATED]
         *
         *  zk挂掉type=CONNECTION_SUSPENDED,，一段时间后type=CONNECTION_LOST
         *  重启zk：type=CONNECTION_RECONNECTED, data=null
         *  更新子节点：type=CHILD_UPDATED, data=ChildData{path='/zktest111/tt1', stat=4294979983,4294979993,1501037475236,1501037733805,2,0,0,0,6,0,4294979983
         , data=[55, 55, 55, 55, 55, 55]}
 ​
         *  删除子节点type=CHILD_REMOVED
         *  更新根节点：不触发
         *  删除根节点：不触发  无异常
         *  创建根节点：不触发
         *  再创建及更新子节点不触发
         *
         * 重启时，与zk连接失败
         */
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final PathChildrenCache watcher = new PathChildrenCache(curatorFramework, ROOT_SHARE_LOCK, true, false, executorService);
        watcher.getListenable().addListener((client, event) -> {
            System.out.println(event);
        });
        /*
        * PathChildrenCache.StartMode说明如下
        * POST_INITIALIZED_EVENT
        * 1、在监听器启动的时候即，会枚举当前路径所有子节点，触发CHILD_ADDED类型的事件
        * 2、同时会监听一个INITIALIZED类型事件
        * NORMAL异步初始化cache
        * POST_INITIALIZED_EVENT异步初始化，初始化完成触发事件PathChildrenCacheEvent.Type.INITIALIZED
        * NORMAL只和POST_INITIALIZED_EVENT的1情况一样，不会ALIZED类型事件触发
    
        * BUILD_INITIAL_CACHE 不会触发上面两者事件,同步初始化客户端的cache，及创建cache后，就从服务器端拉入对应的数据       
         */
        watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        System.out.println("PathChildrenCache监听器注册成功！");
    }

    private void registerNodeWatcher() throws Exception {
        /*
        * 节点路径不存在时，set不触发监听
        * 节点路径不存在，，，创建事件触发监听
        * 节点路径存在，set触发监听
        * 节点路径存在，delete触发监听
        *
        *
        * 节点挂掉，未触发任何监听
        * 节点重连，未触发任何监听
        * 节点重连 ，恢复监听
        */
        final NodeCache watcher = new NodeCache(curatorFramework, ROOT_SHARE_LOCK, false);
        watcher.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("当前节点：" + watcher.getCurrentData());
            }
        });
        //如果为true则首次不会缓存节点内容到cache中，默认为false,设置为true首次不会触发监听事件
        watcher.start();
    }

    private void registerTreeWatcher() throws Exception {
        final TreeCache watcher = new TreeCache(curatorFramework, ROOT_SHARE_LOCK);
        watcher.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework zkClient, TreeCacheEvent event) throws Exception {
                System.out.println("treeCacheEvent" + event);
            }
        });
        //没有开启模式作为入参的方法
        watcher.start();
    }
}
