package com.openmind.annotation;

import java.lang.annotation.*;

/**
 * RateLimiterAspect
 * 限流注解
 *
 * @author zhoujunwen
 * @date 2020-08-07
 * @time 15:36
 * @desc
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiterAspect {
}
