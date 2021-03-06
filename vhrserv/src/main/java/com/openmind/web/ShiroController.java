package com.openmind.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * ShiroController
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 16:06
 * @desc
 */
@Controller
@RequestMapping("/shiro")
public class ShiroController {
    @PostMapping("/login")
    public String doLogin(String username, String password, Model model) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token); // 这里把token放入到shiro上下文subject中
        } catch (AuthenticationException e) {
            model.addAttribute("error", "用户名密码错误");
            return "login_page";
        }

        return "redirect:/index";
    }

    @RequiresRoles("ADMIN")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @GetMapping("/user")
    public String user() {
        return "user";
    }
}
