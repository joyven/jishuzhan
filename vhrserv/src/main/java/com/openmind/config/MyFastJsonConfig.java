package com.openmind.config;

import org.springframework.context.annotation.Configuration;

/**
 * FastjsonConfig
 *
 * @author zhoujunwen
 * @date 2020-01-06
 * @time 22:35
 * @desc
 */
@Configuration
public class MyFastJsonConfig {
    /**
     * HttpMessageConverter 除了使用@Bean的方法定义，还可以实现WebMvcConfigure接口中的configureMessageConverters方法
     *
     * @return
     */
    /*@Bean
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setCharset(UTF_8);
        config.setSerializerFeatures(
                SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty
        );
        converter.setFastJsonConfig(config);

        return converter;
    }*/

}
