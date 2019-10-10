package com.openmind.zookeeper.curator;

import com.openmind.zookeeper.LockStatus;
import com.openmind.zookeeper.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 独享锁(独占锁）
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 11:45
 * @desc
 */
@Slf4j
public class ZkExclusiveLock implements DistributedLock {

    private LockStatus lockStatus;
    private CuratorFramework curatorFramework;

    private static final String ROOT_LOCK = "/exclusive_locks/lock";
    /**
     * 自旋测试超过超时阈值，考虑到网络的延时性，这里设为1000毫秒
     */
    private static final long spinForTimeoutThreshold = 1000L;
    /**
     * 现成休眠100毫秒，重试加锁
     */
    private static final long SLEPP_TIME = 100L;
    private String lockName;
    private CyclicBarrier lockBarrier = new CyclicBarrier(2);
    private String id = String.valueOf(new Random(System.nanoTime()).nextInt(10000000));

    public ZkExclusiveLock(String config, String lockName) throws InterruptedException {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(config)
                .sessionTimeoutMs(100000)
                .retryPolicy(new ExponentialBackoffRetry(100,10,5000))
                .build();
        curatorFramework.start();
        curatorFramework.blockUntilConnected();
        lockStatus = LockStatus.UNLOCK;
        this.lockName = lockName;
    }


    /**
     * 加锁直到成功
     * @throws Exception
     */
    @Override
    public void lock() throws Exception {
        if (lockStatus != LockStatus.UNLOCK) {
            return;
        }

        // 创建锁节点
        if (createLockNode()) {
            log.info("[{} {} {}] 获取了锁", Thread.currentThread().getName(), lockName, id);
            lockStatus = LockStatus.LOCKED;
            return;
        }

        lockStatus = LockStatus.TRY_LOCK;
        lockBarrier.await();
    }

    /**
     * 非重入锁
     * @return
     * @throws Exception
     */
    @Override
    public boolean tryLock() throws Exception {
        if (lockStatus == LockStatus.LOCKED) {
            return true;
        }

        Boolean created = createLockNode();
        lockStatus = created ? LockStatus.LOCKED : LockStatus.UNLOCK;
        return created;
    }

    /**
     * 自旋重入锁，有超时时间
     * @param millisecond
     * @return
     * @throws Exception
     */
    @Override
    public boolean tryLock(long millisecond) throws Exception {
        long millsTimeout = millisecond;
        if (millsTimeout <= 0L) {
            return false;
        }

        final long deadline = System.currentTimeMillis() + millsTimeout;
        for (;;) {
            if (tryLock()) {
                return true;
            }

            /**
             * 考虑到网络的延时性，在加锁的时候，考虑在该阈值内，让线程休眠等待。
             */
            if (millsTimeout > spinForTimeoutThreshold) {
                TimeUnit.MILLISECONDS.sleep(SLEPP_TIME);
            }

            millsTimeout = deadline - System.currentTimeMillis();
            if (millsTimeout <= 0L) {
                return false;
            }
        }
    }

    @Override
    public void unlock() throws Exception {
        if (lockStatus == LockStatus.UNLOCK) {
            return;
        }

        deleteLockNode();
    }

    /**
     * 创建临时节点
     * @return
     */
    private Boolean createLockNode()  {

        try {
            Stat stat = curatorFramework.checkExists().forPath(ROOT_LOCK);
            if (stat != null) {
                curatorFramework.setData().forPath(ROOT_LOCK, "".getBytes());
            } else {
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(ROOT_LOCK, "".getBytes());
            }
            log.info("临时节点已创建");
        } catch (Exception e) {
            log.warn("创建临时节点失败", e);
            return false;
        }
        return true;

    }

    /**
     * 删除路径下的所有的数据，包括子路径
     * @throws Exception
     */
    private void deleteLockNode() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(ROOT_LOCK);
        if (stat != null) {
            curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(ROOT_LOCK);
        }
    }
}
