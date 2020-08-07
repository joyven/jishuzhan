package com.openmind.concurrent;

import java.util.concurrent.Semaphore;

/**
 * SemaphoreTest  控制并发数量
 * <p>
 * 属于一种较常见的限流手段，在实际应用中可以通过信号量机制（如Java中的Semaphore）来实现。
 * 操作系统的信号量是个很重要的概念，Java 并发库 的Semaphore 可以很轻松完成信号量控制，
 * Semaphore可以控制某个资源可被同时访问的个数，通过 acquire() 获取一个许可，如果没有
 * 就等待，而 release() 释放一个许可。
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 14:44
 * @desc
 */
public class SemaphoreTest {
    /**
     * 参数一：控制信号量，许可数量
     * 参数二：是否是公平同步锁
     * Semaphore(10)表示允许10个线程获取许可证，也就是最大并发数是10。
     * Semaphore的用法也很简单，首先线程使用Semaphore的acquire()获取
     * 一个许可证，使用完之后调用release()归还许可证，还可以用tryAcquire()
     * 方法尝试获取许可证，信号量的本质是控制某个资源可被同时访问的个数，
     * 在一定程度上可以控制某资源的访问频率
     */
    private final Semaphore permit = new Semaphore(10, true);

    public void process() {
        try {
            long start = System.currentTimeMillis();
            permit.acquire();
            // 业务逻辑
            long end = System.currentTimeMillis();
            System.out.println(String.format("cost time: %d ms", end - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            permit.release();
        }
    }

}
