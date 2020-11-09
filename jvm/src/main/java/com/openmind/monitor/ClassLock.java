package com.openmind.monitor;

/**
 * ClassLock
 *
 * @author zhoujunwen
 * @date 2020-10-23 13:44
 * @desc
 */
public class ClassLock {
    /**
     * 静态方法内置锁
     */
    public static synchronized void method1() {
        int i = 5;
        while (i-- > 0) {
            System.out.println("[static] " + Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 类元实例内置锁
     */
    public void method2() {
        synchronized (ClassLock.class) {
            int i = 5;
            while (i-- > 0) {
                System.out.println("[class] " + Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 普通方法
     */
    public void method3() {
        int i = 5;
        while (i-- > 0) {
            System.out.println("[normal] " + Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ClassLock classLock = new ClassLock();

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10);
                ClassLock.method1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "test1");

        Thread t2 = new Thread(() -> classLock.method2(), "test2");
        Thread t3 = new Thread(() -> classLock.method3(), "test3");

        t1.start();
        t2.start();
//        t3.start();
    }
}
