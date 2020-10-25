package com.openmind.config;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 *
 * @author zhoujunwen
 * @date 2020-02-24
 * @time 14:32
 * @desc
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnExpression("${swagger.enable:true}")
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {
    private TypeResolver typeResolver;

    @Autowired
    public SwaggerConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean("defaultApi")
    public Docket defaultApi() {
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
        return null;
    }

//    public ApiInfo
}
