package com.openmind.web;

import org.springframework.web.bind.annotation.*;

/**
 * CorsController
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 13:24
 * @desc
 */
@RestController
@RequestMapping("/cors")
public class CorsController {
    @PostMapping("/")
    @CrossOrigin(value = "http://localhost:8081", maxAge = 1800, allowedHeaders = "*")
    public String addBook(String name) {
        return "receive: " + name;
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(value = "http://localhost:8081", maxAge = 1800/*探测请求的有效期*/, allowedHeaders = "*")
    public String deleteBookById(@PathVariable Long id) {
        return String.valueOf(id);
    }
}
