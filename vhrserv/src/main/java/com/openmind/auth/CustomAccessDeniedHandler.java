package com.openmind.auth;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * CustomAccessDeniedHandler
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 23:45
 * @desc
 */
//@Component
public class CustomAccessDeniedHandler /*extends AccessDeniedHandlerImpl*/ implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(403);
        PrintWriter out = response.getWriter();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("msg", "权限不足，无法访问");
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();
    }
}
