package com.openmind.web;

import com.openmind.annotation.RateLimiterAspect;
import com.openmind.service.MethodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MethodController
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 11:44
 * @desc
 */
@RestController
@RequestMapping("/method")
public class MethodController {
    @Resource
    private MethodService methodService;

    @RateLimiterAspect
    @GetMapping("/admin")
    public String admin() {
        return methodService.admin();
    }

    @GetMapping("/dba")
    public String dba() {
        return methodService.dba();
    }

    @GetMapping("/user")
    public String user() {
        return methodService.user();
    }
}
