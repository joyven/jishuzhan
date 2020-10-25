package com.openmind.zookeeper.zkclient;

import com.openmind.zookeeper.DistributedLock;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ComplexDistributeLock
 * zookeeper客户端下多线程线程  时的分布式锁的实现 （一个zookeeper客户端下 线程数 >= 1的情况）
 *
 * @author zhoujunwen
 * @date 2020-09-09
 * @time 11:06
 * @desc
 */
public class ComplexDistributeLock implements DistributedLock {
    // locker节点下所有子节点的名称前缀，实寄创建的名字可能为lock-0000000001
    private static final String LOCK_NODE_NAME_PREFIX = "lock-";
    /**
     * /locker节点路径
     */
    private final String lockerNodePath;
    /**
     * 当前客户端在/locker节点下的子节点 路径
     * 注:创建节点后，此路径并不是创建节点后生成的路径，因为是有序节点，所以会略有不同
     * <p>
     * 如:当前节点路径为为/aspire/abc，那么创建【临时有序】节点后，实际上路径为  /aspire/abc0000000001
     */
    private final String currentNodePath;

    /**
     * 当前客户端在/locker节点下的子节点 的节点名
     * 注:创建节点后，此名字并不是创建节点后生成的名字，因为是有序节点，所以会略有不同
     * <p>
     * 如:当前节点名字为abc，那么创建【临时有序】节点后，实际上名字为   abc0000000001
     */
    private final String currentNodeName;

    /**
     * 网络闪断时的 重试次数
     */
    private static final Integer MAX_RETRY_COUNT = 10;
    /**
     * zk客户端API
     */
    private ZkClient zkClient;

    /**
     * ZooKeeper 客户端对应的使用锁信息
     */
    private final ConcurrentHashMap<ZkClient, NodeInfo> zkClientNodeInfo = new ConcurrentHashMap<>(8);

    /**
     * 构造器
     *
     * @param lockerNodePath
     * @param zkClient
     */
    public ComplexDistributeLock(ZkClient zkClient, String lockerNodePath) {
        this.lockerNodePath = lockerNodePath;
        this.zkClient = zkClient;
        this.currentNodeName = LOCK_NODE_NAME_PREFIX;
        this.currentNodePath = lockerNodePath.concat("/").concat(currentNodeName);
    }

    /**
     * 加锁，直到加锁成功，或者加锁失败抛出异常
     *
     * @throws Exception
     */
    @Override
    public void lock() throws Exception {
        if (!internalLock(-1, null)) {
            throw new IOException("连接丢失！在路径'" + lockerNodePath + "'下不能获取锁");
        }
    }

    /**
     * 尝试加锁，加锁不成功，返回false
     *
     * @return 加锁成功，返回true；加锁失败，返回false
     * @throws Exception
     */
    @Override
    public boolean tryLock() throws Exception {
        return internalLock(0, TimeUnit.MILLISECONDS);
    }

    /**
     * 尝试加锁，设置加锁超时时间，在millisecond内加锁成功或者失败，或者超时跑出异常
     *
     * @param millisecond
     * @return 加锁成功，返回true；加锁失败，返回false
     * @throws Exception 加锁过程异常
     */
    @Override
    public boolean tryLock(long millisecond) throws Exception {
        return internalLock(millisecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 释放锁
     *
     * @throws Exception
     */
    @Override
    public void unlock() throws Exception {
        NodeInfo nodeInfo = zkClientNodeInfo.get(zkClient);
        if (nodeInfo == null) {
            throw new IllegalMonitorStateException("你不是锁：" + lockerNodePath + "的拥有者，无法执行此操作！");
        }
        int newLockCount = nodeInfo.lockCount.decrementAndGet();
        if (newLockCount > 0) { // 当还有其他线程在使用锁时，那么还不能释放
            return;
        }
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("锁计数器已经为负数: " + lockerNodePath);
        }

        try {
            // 只有当计数器为0时，才能正常释放锁
            releaseLock(nodeInfo.nodePath);
        } finally {
            zkClientNodeInfo.remove(zkClient);
        }
    }

    /**
     * 成员内部类---用于封装每个线程的节点数据
     * 注：这里将locker节点下的每一个子节点看做是一个lock
     */
    private static class NodeInfo {
        /**
         * 对应的节点路径
         */
        final String nodePath;

        /**
         * 该客户端内，使用该锁资源的线程数 计数器
         */
        final AtomicInteger lockCount = new AtomicInteger(1);

        private NodeInfo(String nodePath) {
            this.nodePath = nodePath;
        }
    }

    /**
     * 获取锁的公共方法
     * <p>
     * 注:当 time != -1 && unit != null时，才会最多只等待到指定时长，否者会一直等待下去
     *
     * @param time 等待时长
     * @param unit 等待时长的单位
     * @return 是否获取到了锁(如果之前已经获取了锁 ， 那么也会返回true)
     * @throws Exception
     */
    private synchronized boolean internalLock(long time, TimeUnit unit) throws Exception {
        NodeInfo nodeInfo = zkClientNodeInfo.get(zkClient);
        if (nodeInfo != null) { // 如果此线程已经获取了锁
            System.out.println("zookeeper" + zkClient + "已经获得了该锁了！");
            nodeInfo.lockCount.incrementAndGet();
            return true;
        }

        // 如果此线程之前未获得锁
        String nodePath = acquireLock(time, unit);
        if (nodePath != null) {
            NodeInfo newNodeInfo = new NodeInfo(nodePath);
            zkClientNodeInfo.put(zkClient, newNodeInfo);
            return true;
        }
        return false;
    }

    private String acquireLock(long time, TimeUnit unit) throws Exception {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) && (time != -1) ? unit.toMillis(time) : null;
        String finalCurrentNodePath = null;
        boolean gotTheLock = false;

        boolean isDone = false;
        int retryCount = 0;

        // 首次进入，都会进一次下列的代码块；但当网络出现闪断时，会进行循环重试
        while (!isDone) {
            isDone = true;
            try {
                try {
                    // 创建临时有序子节点
                    finalCurrentNodePath = createEphemeralSequentialNode(currentNodePath, null);
                } catch (ZkNoNodeException e) {
                    // 如果父节点不存在，那么先创建父节点，父节点路径即为：lockerNodePath
                    zkClient.createPersistent(lockerNodePath, true);
                    // 再次创建临时有序子节点
                    createEphemeralSequentialNode(currentNodePath, null);
                } catch (ZkNodeExistsException e) {
                    // 由于网络闪断，导致多次进行此步骤的话，则忽略
                }
                gotTheLock = waitToLock(startMillis, millisToWait, finalCurrentNodePath);
            } catch (ZkNoNodeException e) {
                if (retryCount++ < MAX_RETRY_COUNT) {
                    isDone = false;
                } else {
                    throw e;
                }
            }
        }

        if (gotTheLock) {
            return finalCurrentNodePath;
        }
        return null;
    }

    private boolean waitToLock(long startMillis, Long millisToWait, String finalCurrentNodePath) throws Exception {
        boolean gotTheLock = false;
        boolean doDelete = false;
        try {
            while (!gotTheLock) {
                // 获取到/locker节点下的，按照节点名排序后的所有子节点
                List<String> children = getSortedChildren();
                // 获取当前客户端对应的节点的节点名称
                String sequenceNodeName = finalCurrentNodePath.substring(lockerNodePath.length() + 1);
                // 获取当前节点在客户端对应的节点所在集合的位置
                int ourIndex = children.indexOf(sequenceNodeName);
                if (ourIndex < 0) { // 如果集合中不存在该节点，那么抛出异常
                    throw new ZkNoNodeException("节点没找到：" + sequenceNodeName);
                }

                // 当前客户端对应的节点排在集合开头时，表示该客户端获得锁
                boolean shouldGetTheLock = ourIndex == 0;

                if (shouldGetTheLock) {
                    gotTheLock = true;
                } else {
                    // 当前客户端监视的节点的路径
                    String nodeNameToWatch = children.get(ourIndex - 1);
                    // 组装当前客户端应该监视的节点的路径
                    String previousSequencePath = lockerNodePath.concat("/").concat(nodeNameToWatch);
                    // 倒计时锁
                    final CountDownLatch latch = new CountDownLatch(1);
                    // 创建监听器
                    final IZkDataListener previousListener = new IZkDataListener() {
                        @Override
                        public void handleDataChange(String dataPath, Object data) throws Exception {
                            // 数据改变，忽略
                        }

                        @Override
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown();
                        }
                    };

                    try {
                        // 如果节点不存在会出现异常（需要使用重写了ZKClient的watchForData方法客户端）
                        zkClient.subscribeDataChanges(previousSequencePath, previousListener);

                        if (millisToWait != null) { // 如果设置了等待时间，那么最多只等待这么长的时间
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            startMillis = System.currentTimeMillis();
                            if (millisToWait <= 0) {
                                doDelete = true;
                                break;
                            }
                            // CountDownLatch#await
                            latch.await(millisToWait, TimeUnit.MILLISECONDS);
                        } else {
                            // CountDownLatch#await
                            latch.await();
                        }

                    } catch (ZkNoNodeException e) {

                    } finally {
                        zkClient.unsubscribeDataChanges(previousSequencePath, previousListener);
                    }
                }
            }
        } catch (Exception e) {
            doDelete = true;
            throw e;
        } finally {
            if (doDelete) {
                deleteNode(finalCurrentNodePath);
            }
        }
        return gotTheLock;
    }

    /**
     * 创建临时有序节点
     *
     * @param path
     * @param data
     * @return
     */
    private String createEphemeralSequentialNode(final String path, final Object data) {
        return zkClient.createEphemeralSequential(path, data);
    }

    /**
     * 释放锁
     *
     * @author JustryDeng
     * @date 2018/12/6 17:33
     */
    protected void releaseLock(String nodePath) {
        deleteNode(nodePath);
    }

    /**
     * 删除节点
     */
    private void deleteNode(String nodePath) {
        zkClient.delete(nodePath);
    }

    /**
     * 按照子节点名字排序
     *
     * @return
     */
    private List<String> getSortedChildren() {
        try {
            List<String> children = zkClient.getChildren(lockerNodePath);
            children.sort(Comparator.comparing(String::valueOf));
            return children;
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(lockerNodePath, true);
            return getSortedChildren();
        }
    }
}
