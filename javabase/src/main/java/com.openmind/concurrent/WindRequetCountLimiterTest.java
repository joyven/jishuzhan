package com.openmind.concurrent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * WindRequetCountLimiterTest
 * 控制单位时间窗口内请求数
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 16:35
 * @desc
 */
public class WindRequetCountLimiterTest {
    private static final LoadingCache<Long, AtomicLong> counter =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(2, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, AtomicLong>() {
                        @Override
                        public AtomicLong load(Long seconds) {
                            return new AtomicLong(0);
                        }
                    });
    public static long permit = 50;

    public Object getData() throws ExecutionException {
        long currentSeconds = System.currentTimeMillis() / 1000;
        if (counter.get(currentSeconds).incrementAndGet() > permit) {
            Map<String, Object> res = new HashMap<>();
            res.put("code", "100");
            res.put("msg", "访问速度过快");
            return res;
        }
        // 处理业务
        return null; // 返回业务数据
    }
}
