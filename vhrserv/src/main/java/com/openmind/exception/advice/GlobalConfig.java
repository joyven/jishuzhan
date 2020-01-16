package com.openmind.exception.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalConfig 全局数据绑定
 *
 * @author zhoujunwen
 * @date 2020-01-13
 * @time 22:58
 * @desc
 */
@ControllerAdvice
public class GlobalConfig {
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
