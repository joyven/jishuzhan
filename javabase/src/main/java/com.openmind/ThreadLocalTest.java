package com.openmind;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * jishuzhan
 *
 * Exception in thread "main" *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding"
 * with message can't create byte arrau at JPLISAgent.c line: 813
 * *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message can't create
 * byte arrau at JPLISAgent.c line: 813
 *
 * Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
 * @author zhoujunwen
 * @date 2019-12-27
 * @time 16:11
 * @desc
 */
public class ThreadLocalTest {
    static ThreadLocal threadLocal = new ThreadLocal();
    static Integer MOCK_MAX = 10000;
    static Integer THREAD_MAX = 100;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_MAX);
        for (int i = 0; i < THREAD_MAX; i++) {
            executorService.submit(()->{
                threadLocal.set(new ThreadLocalTest().getList());
                System.out.println(Thread.currentThread().getName());
            });

            try {
                 Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    List getList() {
        List list = new ArrayList();
        for (int i = 0; i < MOCK_MAX; i++) {
            list.add("Version：JDK 8");
            list.add("ThreadLocal");
            list.add("Author：Joyven");
            list.add("DateTime：" + LocalDateTime.now());
            list.add("Test：ThreadLocal OOM");
        }
        return list;
    }
}
