package com.openmind.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * MyCommandLineRunner
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 15:29
 * @desc
 */
@Component
@Order(2)
public class MyCommandLineRunner2 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>MyCommandLineRunner2<<<<<<<<" + Arrays.toString(args));
    }
}
