package com.openmind.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * BeanConfig
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 15:24
 * @desc
 */
@Configuration
public class BeanConfig {
    @Bean
    @ConditionalOnBean(MultipartProperties.class)
    MultipartConfigElement multipartConfigElement(MultipartProperties multipartProperties) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(multipartProperties.getMaxFileSize()); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize(multipartProperties.getMaxRequestSize());
        factory.setLocation(multipartProperties.getLocation());
        factory.setFileSizeThreshold(multipartProperties.getFileSizeThreshold());
        return factory.createMultipartConfig();
    }
}
