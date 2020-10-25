package com.openmind;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 测试多线程下，SimpleDateFormat线程不安全的问题
 */
public class SimpleDateFormatTest {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final AtomicLong tNums = new AtomicLong(0);
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 100, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1024), new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, String.format("pool-%d", tNums.getAndIncrement()));
//            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println(String.format("%s, %s", t.getName(), e.getMessage()));
                }
            });
            return thread;
        }

    }/*new ThreadFactoryBuilder().setNameFormat("pool-%d").build()*/, /*new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                Thread t = new Thread(r, "临时线程执行");
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/ new ThreadPoolExecutor.DiscardPolicy());


    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    public void testNoSafe() {
        while (true) {
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String dateString = simpleDateFormat.format(new Date());
//                    System.out.println(dateString);
                    try {
                        Date parseDate = simpleDateFormat.parse(dateString);
                        String dateString2 = simpleDateFormat.format(parseDate);
                        System.out.println(String.format("%s,%s,%s", dateString, dateString2, dateString.equals(dateString2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void testSafe() {
        while (true) {
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat sdf = null;
                    sdf = threadLocal.get();
                    if (sdf == null) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }

                    String dateString = sdf.format(new Date());
                    try {
                        Date parseDate = sdf.parse(dateString);
                        String dateString2 = sdf.format(parseDate);
                        System.out.println(String.format("%s,%s,%s", dateString, dateString2, dateString.equals(dateString2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public static void main(String[] args) {
        new SimpleDateFormatTest().testSafe();
    }
}
