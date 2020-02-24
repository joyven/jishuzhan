package com.openmind;

/**
 * jishuzhan
 *
 * @author zhoujunwen
 * @date 2019-12-26
 * @time 11:33
 * @desc
 */
public class DeadLockTest {
    public static void main(String[] args) throws InterruptedException {
        Object o1 = new Object();
        Object o2 = new Object();

        new Thread(() -> {
            synchronized (o1) {
                try {
                    System.out.println(System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println(System.currentTimeMillis() + " 释放锁");
                    o1.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName());
                }

            }
        }).start();

        new Thread(()->{
            synchronized (o2) {
                try {
                    System.out.println(System.currentTimeMillis());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (o1) {
                    System.out.println(System.currentTimeMillis() + " " +  Thread.currentThread().getName());
                }
            }
        }).start();

//        o1.notify();
    }
}
