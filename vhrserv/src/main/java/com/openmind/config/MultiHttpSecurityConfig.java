package com.openmind.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MultiHttpSecurityConfig
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 17:47
 * @desc
 */
//@Configuration
public class MultiHttpSecurityConfig {
    //    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //    @Configuration
//    @Order(10)
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN");
        }
    }

    //    @Configuration
//    @Order(11)
    public static class OtherSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("root").password("root").roles("ADMIN", "DBA") // root用户拥有ADMIN DBA 权限
                    .and()
                    .withUser("admin").password("123456").roles("ADMIN", "USER") // admin用户用于ADMIN USER 权限
                    .and()
                    .withUser("zjw").password("123").roles("USER"); // zjw拥有USER权限
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login_page")
                    .loginProcessingUrl("/login")
                    .usernameParameter("name")
                    .passwordParameter("passwd")
                    .permitAll()
                    .and()
                    .csrf()
                    .disable();
        }
    }
}
