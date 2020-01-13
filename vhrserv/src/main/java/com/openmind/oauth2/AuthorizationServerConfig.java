package com.openmind.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * AuthorizationServerConfig
 * 配置授权服务器
 * OAuth2.0支持四种授权模式：
 * - 授权码模式(authorization code)：
 * ① 客户端请求授权<==>用户同意授权 ②
 * ③ 客户端申请令牌<==>授权服务器发放令牌 ④
 * ⑤ 客户端申请资源<==>资源服务器开发资源 ⑥
 * - 简化模式：不需要客户端与服务器端参与，直接在浏览器中向授权服务器申请令牌（静态网站）
 * - 密码模式：用户把密码和用户名直接告诉客户端，客户端使用这些信息向授权服务器申请令牌。（同一家服务）
 * - 客户端模式：不需要用户参与，客户端以自己的名义向服务器提供者申请授权。
 * <p>
 * 授权： POST方式：https://localhost/oauth/token?username=user&password=123&grant_type=password&client_id=password&scope=all&client_secret=123
 * 刷新Token： POST方式：https://localhost/oauth/token?username=user&password=123&grant_type=refresh_token&client_id=password&scope=all&client_secret=123&refresh_token=b75a4ce4-637b-443d-8ff9-8acf7bb4f7c3
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 11:50
 * @desc
 */
//@Configuration
//@EnableAuthorizationServer // 开启授权服务器
public class AuthorizationServerConfig implements AuthorizationServerConfigurer {
    /**
     * 该对象用来支持password模式
     */
    @Autowired
    AuthenticationManager authenticationManager;
    /**
     * 存放令牌
     */
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    /**
     * 提供刷新token的支持
     */
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); // 支持 client_id 和 client_secret 做登录认证
    }

    /**
     * ① 配置授权模式， withClient中指明了password授权模式。
     * ② authorizedGrantType 标识 OAuth2 中授权模式为 "passowrd" 和 "refresh_token"两种，在标准的OAuth2中授权模式
     * 并不包括refresh_token，但spring security 的实现将其归为一种，因此如果要实现 access_token 的过期刷新，就需要
     * 添加refresh_token授权模式。
     * ③ accessTokenValiditySeconds 方法配置了 access_token 的过期时间。
     * ④ resourceIds 配置了资源的id
     * ⑤ secret 方法配置了加密后的密码，明文 123
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("CLI-1234567890") //
                .authorizedGrantTypes("password", "refresh_token") //
                .accessTokenValiditySeconds(1800) // 配置了 access_token 的过期时间。
                .resourceIds("resource_id") // resourceIds 配置了资源的id
                .scopes("all")
                .secret("$2a$10$GGiV4JWfeTQa2avIAzWkTOhrJo53zB2OX0bJ1yrNS9GhOyq0T7i1O"); // secret 方法配置了加密后的密码，明文 123
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
