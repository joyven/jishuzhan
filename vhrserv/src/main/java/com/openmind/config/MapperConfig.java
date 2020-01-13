package com.openmind.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MapperConfig
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 12:38
 * @desc
 */
@Configuration
@MapperScan("com.openmind.dao")
public class MapperConfig {
}
