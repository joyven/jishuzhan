package com.openmind.concurrent;

/**
 * RateLimiterTest
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 14:59
 * @desc
 */
public class RateLimiterTest {
    public static void main(String[] args) {
        GuavaRateLimiterTest test = new GuavaRateLimiterTest();
//        test.smoothBurstyProcess();
        test.smoothWarmingUpProcessor();
    }
}
