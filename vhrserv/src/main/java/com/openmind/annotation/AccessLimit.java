package com.openmind.annotation;

import java.lang.annotation.*;

/**
 * AccessLimit
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 17:00
 * @desc
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    // 窗口时间内允许通过的最大请求
    int limit() default 50;

    // 窗口时间
    int sec() default 10;
}
