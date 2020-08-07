package com.openmind.web;

import com.openmind.annotation.RateLimiterAspect;
import com.openmind.vo.Author;
import com.openmind.vo.Book;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * UserController
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 10:30
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RateLimiterAspect
    @GetMapping("/userInfo")
    public void getUserInfo(Model model) {
        Map<String, Object> map = model.asMap();
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = map.get(key);
            System.out.println(">>>>>key=" + key + ",>>>>value=" + value);
        }
        return;
    }

    @GetMapping("user")
    public String book(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author) {
        return book.toString() + ">>>" + author.toString();
    }

    @GetMapping("bcrypt")
    public String bcrypt(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePasswd = encoder.encode(password);
        System.out.println("username=" + username + ", password=" + encodePasswd);
        return encodePasswd;
    }
}
