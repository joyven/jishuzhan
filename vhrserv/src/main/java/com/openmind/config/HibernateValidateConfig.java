package com.openmind.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Properties;

/**
 * HibernateValidateConfig
 *
 * @author zhoujunwen
 * @date 2020-02-24
 * @time 14:58
 * @desc
 */
@Configuration
public class HibernateValidateConfig {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource msgsource = new ResourceBundleMessageSource();
        msgsource.setBasename("messages");
        return msgsource;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        //failFast：true  快速失败返回模式(只要有一个验证失败，则返回)    false 普通模式(会校验完所有的属性，然后返回所有的验证失败信息)
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());

        Properties properties = new Properties();
        properties.put("hibernate.validator.fail_fast", "true");
        bean.setValidationProperties(properties);
        return bean;
    }

   /* @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }*/

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        //设置validator模式为快速失败返回
        postProcessor.setValidator(localValidatorFactoryBean());
        return postProcessor;
    }
}
