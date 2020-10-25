package com.openmind;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * UseVolatileTest
 *
 * @author zhoujunwen
 * @date 2020-09-09
 * @time 15:57
 * @desc
 */
public class UseVolatileTest implements Runnable {
    volatile int done = 0;
    AtomicInteger real = new AtomicInteger();

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            setDone(i);
            real.incrementAndGet();
        }
    }

    private void setDone(int i) {
        done = i;
    }


    public static void main(String[] args) throws InterruptedException {
        Runnable r = new UseVolatileTest();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(((UseVolatileTest) r).done);
        System.out.println(((UseVolatileTest) r).real.get());
    }
}
