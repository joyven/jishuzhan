package com.openmind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 基于数据库动态权限配置
 * 创建五张表：
 * user(用户)-->user_role(用户角色映射)<--role(角色)-->menu_role(菜单角色映射)<--menu(菜单)
 * <p>
 * EnableGlobalMethodSecurity 方法安全，prePostEnabled激活@PreAuthorize和@PostAuthorize注解，securedEnabled激活@Secured注解
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 16:24
 * @desc
 */
//@Order(1)
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class Oauth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    @Order(1)
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$10$Ab4GhmfyRSaRkmEOEZGAveRQwu0B8HTSF68BKoJiLKp5a2gYGj3qi")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("$2a$10$h6siaFhBiiaTU4PEWwf0meaKvPYOZQjNmQAJuGnVz8PGG/M/bdxUm")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/oauth/**").authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and().csrf().disable();
    }
}
