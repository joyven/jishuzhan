package com.openmind.limiter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * RateLimiterAop
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 15:38
 * @desc
 */
public class RateLimiterAop {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterAop.class);
    @Autowired
    private HttpServletResponse response;
    private RateLimiter rateLimiter = RateLimiter.create(5.0);

    @Pointcut("@annotation(com.openmind.annotation.RateLimiterAspect)")
    public void pointCut() {
    }

    @Around("serverLimit()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        try {
            if (flag) {
                obj = joinPoint.proceed();
            } else {
                Map<String, Object> map = new HashMap(4);
                map.put("code", HttpStatus.CREATED.value());
                map.put("message", "慢点好吗，请温柔待我");
                String result = JSONObject.toJSONString(map);
                output(response, result);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.info("flag={} ,obj= {}", flag, obj);
        return obj;
    }

    void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(msg.getBytes("UTF-8"));
            outputStream.flush();
        }
    }
}
