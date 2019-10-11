package com.openmind.zookeeper;

import com.openmind.zookeeper.zknative.ZkReadWriteLock;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-11
 * @time 15:33
 * @desc
 */
public class ZkNativeReadLockTest {
    String config = "192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181";
    private ReadLockTest readLockTest = new ReadLockTest();
    private WriteLockTest writeLockTest= new WriteLockTest();

    @Test
    public void testReadLock() throws Exception {
       readLockTest.lock();
       readLockTest.tryLock();
       readLockTest.tryLockTimeout();
       readLockTest.unlock();
    }

    @Test
    public void testWriteLock() throws Exception {
        writeLockTest.lock();
        writeLockTest.tryLock();
        writeLockTest.tryLock1();
        writeLockTest.unlock();
    }


     class ReadLockTest {
        @Test
        public void lock() throws Exception {
            Runnable runnable = () -> {
                try {
                    ZkReadWriteLock lock = new ZkReadWriteLock(config);
                    lock.readLock().lock();
                    Thread.sleep(1000 + new Random(System.nanoTime()).nextInt(2000));
                    lock.readLock().unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            int poolSize = 4;
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Thread.sleep(10);
                executorService.submit(runnable);
            }

            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }

        @Test
        public void tryLock() throws Exception {
            ZkReadWriteLock lock = new ZkReadWriteLock(config);
            Boolean locked = lock.readLock().tryLock();
            System.out.println("locked: " + locked);
            lock.readLock().unlock();
        }

        @Test
        public void tryLockTimeout() throws Exception {
            ZkReadWriteLock lock = new ZkReadWriteLock(config);
            Boolean locked = lock.readLock().tryLock(20000);
            System.out.println("locked: " + locked);
            lock.readLock().unlock();
        }

        @Test
        public void unlock() throws Exception {
        }
    }

    class WriteLockTest {
        @Test
        public void lock() throws Exception {
            Runnable runnable = () -> {
                try {
                    ZkReadWriteLock srwl = new ZkReadWriteLock(config);
                    srwl.writeLock().lock();
                    Thread.sleep(1000 + new Random(System.nanoTime()).nextInt(2000));
                    srwl.writeLock().unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            int poolSize = 4;
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Thread.sleep(10);
                executorService.submit(runnable);
            }

            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }

        @Test
        public void tryLock() throws Exception {
            ZkReadWriteLock srwl = new ZkReadWriteLock(config);
            Boolean locked = srwl.writeLock().tryLock();
            System.out.println("locked: " + locked);
            srwl.writeLock().unlock();
        }

        @Test
        public void tryLock1() throws Exception {
            ZkReadWriteLock srwl = new ZkReadWriteLock(config);
            Boolean locked = srwl.writeLock().tryLock(20000);
            System.out.println("locked: " + locked);
            srwl.writeLock().unlock();
        }

        @Test
        public void unlock() throws Exception {
        }
    }

}
