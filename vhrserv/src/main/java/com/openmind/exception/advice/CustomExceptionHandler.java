package com.openmind.exception.advice;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CustomExceptionHandler
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 09:30
 * @desc
 */
@ControllerAdvice
public class CustomExceptionHandler {
    /*@ExceptionHandler(MaxUploadSizeExceededException.class)
    public void uploadException(MaxUploadSizeExceededException e, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("上传文件大小超出限制！");
        writer.flush();
        writer.close();
    }*/

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView uploadException(MaxUploadSizeExceededException e) throws IOException {
        ModelAndView view = new ModelAndView();
        view.addObject("msg", "上传文件大小超限制！");
        view.setViewName("error");
        return view;
    }

    /**
     * shiro认证异常全局拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView error(AuthorizationException e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("error", e.getMessage());
        return mv;
    }

    @ModelAttribute(value = "userInfo")
    public Map<String, String> getUserInfo() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("gender", "男");
        userInfo.put("username", "张三");
        return userInfo;
    }

    /**
     * 数据绑定测试demo
     *
     * @param binder
     */
    @InitBinder("b")
    public void init(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("b.");
    }

    @InitBinder("a")
    public void init2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("a.");
    }

}
