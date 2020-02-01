package com.openmind.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * MyFilter
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 15:43
 * @desc
 */
@WebFilter("/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter>>>>init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("MyFilter>>>>>doFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter>>>>>destroy");
    }
}
