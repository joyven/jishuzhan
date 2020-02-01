package com.openmind.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * RessoureServerConfig
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 12:21
 * @desc
 */
//@Configuration
//@EnableResourceServer // 开启资源服务配置
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 这里的资源ID 需要和授权服务中的资源ID一致
        resources.resourceId("resource_id").stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/method/admin").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasAnyRole('ADMIN','DBA')")
                .antMatchers("/method/user").hasRole("USER")
                .anyRequest().authenticated();
    }
}
