package com.openmind.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;

/**
 * ShiroConfig  TextConfigurationRealm 方式简单实现
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 16:00
 * @desc
 */
//@Configuration
public class ShiroConfig {
    /**
     * ① 用户和角色的权限，可以根据情况改为从数据中获取
     *
     * @return
     */
    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("zjw=123,user\n admin=123,admin");
        realm.setRoleDefinitions("admin=read,write\n user=read");
        return realm;
    }

    /**
     * ② 基本的过滤规则
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/login_page", "anon");
        definition.addPathDefinition("/shiro/login", "anon");
        definition.addPathDefinition("/logout", "anon");
        definition.addPathDefinition("/**", "authc");

        return definition;
    }

    /**
     * shiro标签在freemarker中的扩展
     */
//    @Bean("freeMarkerConfigurer")
//    @Order(1)
//    FreeMarkerConfigurer freeMarkerConfigurer() {
//        FreeMarkerConfigurer fmc = new FreeMarkerConfigurer() {
//            @Override
//            public void afterPropertiesSet() throws IOException, TemplateException {
//                super.afterPropertiesSet();
//                freemarker.template.Configuration cfg = this.getConfiguration();
//                // 添加 shiro 标签
//                cfg.setSharedVariable("shiro", new ShiroTags());
//            }
//        };
//
//        fmc.setTemplateLoaderPath("classpath:/templates/");
//        fmc.setFreemarkerSettings(new Properties() {{
//            put("template_update_delay", "0");
//            put("default_encoding", "UTF-8");
//            put("locale", "zh_CN");
//            put("boolean_format", "true,false");
//            put("datetime_format", "yyyy-MM-dd HH:mm:ss");
//            put("date_format", "yyyy-MM-dd");
//            put("time_format", "HH:mm:ss");
//            put("number_format", "0.##########");
//            put("classic_compatible", "true");
//            put("template_exception_handler", "ignore");
////            put("auto_import", Arrays.asList("/common/_meta.ftl as _meta").spliterator());
//        }});
//        return fmc;
//    }

}
