package com.openmind.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author zhoujunwen
 * @date 2020-01-06
 * @time 20:54
 * @desc
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
