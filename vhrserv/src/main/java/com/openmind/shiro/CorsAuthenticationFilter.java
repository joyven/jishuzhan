package com.openmind.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * CorsAuthenticationFilter
 *
 * @author zhoujunwen
 * @date 2020-01-16
 * @time 11:13
 * @desc
 */
public class CorsAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean isAllowed = super.isAccessAllowed(request, response, mappedValue);
        if (!isAllowed) {
            // 判断请求是否是OPTIONS
            String method = WebUtils.toHttp(request).getMethod();
            if (StringUtils.equalsIgnoreCase("OPTIONS", method)) {
                return true;
            }
        }

        return isAllowed;
    }
}
