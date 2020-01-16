package com.openmind.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * MapperConfig
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 12:38
 * @desc
 */
@Configuration
@Order(1)
@MapperScan("com.openmind.dao")
public class MapperConfig {
}
