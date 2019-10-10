package com.openmind.zookeeper;

import com.openmind.zookeeper.zookeeper.ZkExclusiveLock;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-10
 * @time 14:17
 * @desc
 */
public class ExclusiveLockTest {
    String zkPath = "192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181";
    @Test
    public void lock() throws Exception {
        Runnable runnable = () -> {
            try {
                DistributedLock lock = new ZkExclusiveLock(zkPath);
                lock.lock();
                Thread.sleep(4000);
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        int poolSize = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executorService.submit(runnable);
        }

        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void tryLock() throws Exception {
        ZkExclusiveLock lock = new ZkExclusiveLock(zkPath);
        Boolean locked = lock.tryLock();
        System.out.println("locked: " + locked);
    }

    @Test
    public void tryLock1() throws Exception {
        ZkExclusiveLock lock = new ZkExclusiveLock(zkPath);
        Boolean locked = lock.tryLock(50000);
        System.out.println("locked: " + locked);
    }

    @Test
    public void unlock() throws Exception {
    }
}
