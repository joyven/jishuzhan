package com.openmind.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * FormLoginFilter
 * 如果要实现前后端分离,配置shiro拦截器实现返回状态码的需求
 * 可以设置为json格式
 *
 * @author zhoujunwen
 * @date 2020-01-14
 * @time 13:27
 * @desc
 */
public class FormLoginFilter extends PathMatchingFilter {
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthenticated = subject.isAuthenticated();
        if (!isAuthenticated) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("NO AUTH!");
            return false;
        }
        return true;
    }
}
