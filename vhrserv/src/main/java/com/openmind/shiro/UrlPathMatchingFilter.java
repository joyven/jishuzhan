package com.openmind.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * UrlPathMatchingFilter
 *
 * @author zhoujunwen
 * @date 2020-01-16
 * @time 10:27
 * @desc
 */
public class UrlPathMatchingFilter extends PathMatchingFilter {
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 请求URL
        String requestUrl = getPathWithinApplication(request);
        Subject subject = SecurityUtils.getSubject();


        if (!subject.isAuthenticated()) {
            // 如果未授权，则直接返回true，进入登录流程
            return true;
        }

        return true;
    }
}
