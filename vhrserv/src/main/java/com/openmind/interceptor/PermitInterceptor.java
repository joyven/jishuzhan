package com.openmind.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PermitInterceptor
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 15:21
 * @desc
 */
@Slf4j
public class PermitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>>>>> {}#{}", PermitInterceptor.class.getSimpleName(), "preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info(">>>>>> {}#{}", PermitInterceptor.class.getSimpleName(), "postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        log.info(">>>>>> {}#{}", PermitInterceptor.class.getSimpleName(), "afterCompletion");
    }
}
