package com.openmind.monitor;

/**
 * IntrinsicLock 内置锁
 *
 * @author zhoujunwen
 * @date 2020-10-23 12:53
 * @desc
 */
public class IntrinsicLock {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        SampleClass sampleClass1 = new SampleClass();
        SampleClass sampleClass2 = new SampleClass();

        Thread t1 = new Thread1(sampleClass1);
        Thread t2 = new Thread2(sampleClass1);
//        Thread t2 = new Thread2(sampleClass2);

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.start();
    }
}

class SampleClass {
    public synchronized void sayHello() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello!");
    }

    public synchronized void sayWorld() {
        System.out.println("World!");
    }
}

class Thread1 extends Thread {
    private SampleClass sampleClass;

    public Thread1(SampleClass sampleClass) {
        this.sampleClass = sampleClass;
    }

    @Override
    public void run() {
        sampleClass.sayHello();
    }
}

class Thread2 extends Thread {
    private SampleClass sampleClass;

    public Thread2(SampleClass sampleClass) {
        this.sampleClass = sampleClass;
    }

    @Override
    public void run() {
        sampleClass.sayWorld();
    }
}
