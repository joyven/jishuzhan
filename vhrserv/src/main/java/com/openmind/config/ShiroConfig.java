package com.openmind.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ShiroConfig
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 16:00
 * @desc
 */
@Configuration
public class ShiroConfig {
    /**
     * 用户和角色的权限，可以根据情况改为从数据中获取
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
     * 基本的过滤规则
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/login_page", "anon");
        definition.addPathDefinition("/login", "anon");
        definition.addPathDefinition("/logout", "anon");
        definition.addPathDefinition("/**", "authc");

        return definition;
    }
}
