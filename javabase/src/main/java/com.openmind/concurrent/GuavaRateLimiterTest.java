package com.openmind.concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * GuavaRateLimiterTest
 * 控制访问速率
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 14:53
 * @desc
 */
public class GuavaRateLimiterTest {
    public void smoothBurstyProcess() {
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        /**
         * 首先通过RateLimiter.create(1.0);创建一个限流器，参数代表每秒生成的令牌数，
         * 通过limiter.acquire(i);来以阻塞的方式获取令牌，令牌桶算法允许一定程度的突
         * 发（允许消费未来的令牌），所以可以一次性消费i个令牌；当然也可以通过
         * tryAcquire(int permits, long timeout, TimeUnit unit)来设置等待超时
         * 时间的方式获取令牌，如果超timeout为0，则代表非阻塞，获取不到立即返回，支持阻塞或可超时的令牌消费。
         */
        // RateLimiter limiter = RateLimiter.create(1.0);
        /**
         * RateLimiter.create(5)表示桶容量为5且每秒新增5个令牌，即每隔200毫秒新增一个令牌；
         * limiter.acquire()表示消费一个令牌，如果当前桶中有足够令牌则成功（返回值为0），
         * 如果桶中没有令牌则暂停一段时间，比如发令牌间隔是200毫秒，则等待200毫秒后再去消费令牌，
         * 这种实现将突发请求速率平均为了固定请求速率。
         */
        RateLimiter limiter = RateLimiter.create(5);
        double pre = 0.0d;
        for (int i = 1; i <= 10; i++) {
            double waitTime = limiter.acquire(i);
            System.out.println(String.format("cutTime=%d call execute: %d waitTime:%f, preDiff: %f", System.currentTimeMillis(),
                    i, waitTime, waitTime - pre));
            pre = waitTime;
        }
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("start time: " + start);
        System.out.println("end time: " + end);
    }

    public void smoothWarmingUpProcessor() {
        RateLimiter limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
        double pre = 0.0d;
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for (int i = 1; i <= 10000; i++) {
            double waitTime = limiter.acquire(i);
            System.out.println(String.format("cutTime=%d call execute: %d waitTime:%f, preDiff: %f", System.currentTimeMillis(),
                    i, waitTime, waitTime - pre));
            pre = waitTime;
        }
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("start time: " + start);
        System.out.println("end time: " + end);
    }
}
