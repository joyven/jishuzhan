package com.openmind.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * MyApplicattionRunner
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 15:33
 * @desc
 */
@Component
@Order(3)
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>>>>>>>>MyApplicationRunner<<<<<<<<<" + Arrays.toString(args.getSourceArgs()));
    }
}
