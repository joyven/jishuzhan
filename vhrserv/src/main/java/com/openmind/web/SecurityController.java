package com.openmind.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecurityController
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 16:43
 * @desc
 */
@RestController
public class SecurityController {
    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin!";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user!";
    }

    @GetMapping("/dba/hello")
    public String dba() {
        return "hello dba!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
