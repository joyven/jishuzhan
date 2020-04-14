package com.openmind.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.openmind.helper.PasswordHelper;
import com.openmind.service.UserService;
import com.openmind.shiro.CorsAuthenticationFilter;
import com.openmind.shiro.FormLoginFilter;
import com.openmind.shiro.UrlPathMatchingFilter;
import com.openmind.shiro.UserRealm;
import com.openmind.vo.Menu;
import freemarker.template.TemplateException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 权限控制有:
 * - 注解的方式
 * - shiro 标签的方式
 * - url 动态控制的方式
 *
 * @author zhoujunwen
 * @date 2020-01-09
 * @time 16:00
 * @desc
 */
@Configuration
public class ShiroConfig2 {
    @Autowired
    UserService userService;

    /**
     * 配置 Bean 后置处理器: 会自动的调用和 Spring 整合后各个组件的生命周期方法.
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ① 用户和角色的权限，可以根据情况改为从数据中获取
     *
     * @return
     */
    @Bean
    public Realm realm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(bCryptCredentialsMatcher());
//        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * ② 基本的过滤规则
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/login_page", "anon");
//        chainDefinition.addPathDefinition("/shiro/login", "anon");

        /*// 三种方式实现定义权限路径
        // 第一种:使用角色名定义
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
        // 第二种:使用权限code定义
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
        // 第三种:使用接口的自定义配置(此处配置之后需要在对应的接口使用@RequiresPermissions(""))
        chainDefinition.addPathDefinition("/**", "authc");*/

        //不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,但是此处必须存在,
        // 因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
        return chainDefinition;
    }

    /**
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
//        shiroFilter.setSecurityManager(securityManager());

        // 添加自己的过滤器
        Map<String, Filter> filters = shiroFilter.getFilters();
        filters.put("cors", new CorsAuthenticationFilter());
        filters.put("authc", new FormLoginFilter());
        filters.put("req", new UrlPathMatchingFilter());

        // 系统拦截器配置,等同于shiroFilterChainDefinition()中的配置
        shiroFilter.setLoginUrl("/login_page");
        shiroFilter.setSuccessUrl("/index");
        shiroFilter.setUnauthorizedUrl("/error");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");


        // 从数据库中获取资源配置，加入到拦截器，实现动态拦截
        List<Menu> menus = null;//shiroService.selectAll();
        /*for (Menu menu : menus) {
            List<Role> roles = menu.getRoles();
            Set<String> roleNames = roles.stream()
                    .map(r -> StringUtils.replace(r.getName(), "ROLE_", ""))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(roleNames)) {
                filterChainDefinitionMap.put(menu.getPattern(), "authc, roles[" + StringUtils.join(roleNames, ",") + "]");
//                 String permission = "perms[" + resources.getResurl()+ "]";
//                 filterChainDefinitionMap.put(resources.getResurl(),permission);
            }
            // 如果有资源code，还可以加入资源code
            // filterChainDefinitionMap.put(menu.getPattern(), "authc, perms[document:read]");
        }*/

//        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilter;
    }

    /*RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setPassword(redisProperties.getPassword());
        redisManager.setDatabase(redisProperties.getDatabase());
        redisManager.setTimeout(redisProperties.getTimeout() != null ? redisProperties.getTimeout().getNano() : 3600);

        return redisManager;
    }*/


    @Bean
    SessionDAO sessionDAO() {
        return new MemorySessionDAO();
        /*RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setPassword(redisProperties.getPassword());
        redisManager.setDatabase(redisProperties.getDatabase());
        redisManager.setTimeout(redisProperties.getTimeout() != null ? redisProperties.getTimeout().getNano() : 3600);


        redisSessionDAO.setRedisManager(redisManager);*/
//        return redisSessionDAO;
    }

//    @Bean
//    @Order(1)
//    SessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionDAO(sessionDAO());
//        sessionManager.setCacheManager(cacheManager());
//        return sessionManager;
//    }

    /**
     * 方法声明的时候类型必须是 DefaultWebSecurityManager，如果是SecurityManager则会报错：
     * required a bean named 'authenticator' that could not be found
     *
     * @return
     */
//    @Bean
//    @Order(1)
//    DefaultWebSecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置realm
//        securityManager.setRealm(realm());
//        // 设置session管理器
//        securityManager.setSessionManager(sessionManager());
//        securityManager.setCacheManager(cacheManager());
//        securityManager.setRememberMeManager(rememberMeManager());
//        return securityManager;
//    }

    /**
     * 缓存管理器
     *
     * @return
     */
//    @Bean
//    CacheManager cacheManager() {
//        return new MemoryConstrainedCacheManager();
//
//        /*RedisCacheManager cacheManager = new RedisCacheManager();
//
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
//        redisManager.setPassword(redisProperties.getPassword());
//        redisManager.setDatabase(redisProperties.getDatabase());
//        redisManager.setTimeout(redisProperties.getTimeout() != null ? redisProperties.getTimeout().getNano() : 3600);
//
//
//        cacheManager.setRedisManager(redisManager);
//        cacheManager.setExpire(1800);
//
//        return cacheManager;*/
//    }

    /**
     * 记住我
     *
     * @return
     */
    /*@Bean
    RememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(cookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        rememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return rememberMeManager;
    }*/

    /*@Bean
    Cookie cookie() {
        SimpleCookie cookie = new SimpleCookie();
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        cookie.setName("rememberMe");
        // 记住我cookie生效时间30天 ,单位秒;
        cookie.setMaxAge(2592000);

        return cookie;
    }*/

    /**
     * shiro标签在freemarker中的扩展ShiroAnnotationProcessorAutoConfiguration
     */
    @Bean("freeMarkerConfigurer")
    @Order(1)
    FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer fmc = new FreeMarkerConfigurer() {
            @Override
            public void afterPropertiesSet() throws IOException, TemplateException {
                super.afterPropertiesSet();
                freemarker.template.Configuration cfg = this.getConfiguration();
                // 添加 shiro 标签
                cfg.setSharedVariable("shiro", new ShiroTags());
            }
        };

        fmc.setTemplateLoaderPath("classpath:/templates/");
        fmc.setFreemarkerSettings(new Properties() {{
            put("template_update_delay", "0");
            put("default_encoding", "UTF-8");
            put("locale", "zh_CN");
            put("boolean_format", "true,false");
            put("datetime_format", "yyyy-MM-dd HH:mm:ss");
            put("date_format", "yyyy-MM-dd");
            put("time_format", "HH:mm:ss");
            put("number_format", "0.##########");
            put("classic_compatible", "true");
            put("template_exception_handler", "ignore");
//            put("auto_import", "/common/_meta.ftl as _meta");
        }});
        return fmc;
    }

    /**
     * 密码匹配器，这是为了和spring security兼容，使用了bcypt算法
     * 针对的不同的情况，可以使用不同的方式，比如md5，md5+salt等
     *
     * @return
     */
    private CredentialsMatcher bCryptCredentialsMatcher() {
        return (token, info) -> {
            UsernamePasswordToken userToken = (UsernamePasswordToken) token;
            //要验证的明文密码
            String plaintext = new String(userToken.getPassword());
            //数据库中的加密后的密文
            String hashed = info.getCredentials().toString();
            /**
             * 我们也可以引入 de.svenkubiak:jBCrypt:0.4.1的包
             */
            return PasswordHelper.matcher(plaintext, hashed);
        };
    }

    /**
     * 这是一个md5的密码配置匹配器，暂时没有
     *
     * @return
     */
//    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        /**
         * hash算法:这里使用MD5算法;
         */
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        /**
         * 散列的次数，比如散列两次，相当于 md5(md5(""));
         */
        hashedCredentialsMatcher.setHashIterations(1);

        return hashedCredentialsMatcher;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    /*@Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }*/

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
   /* public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("kickout");
        return kickoutSessionControlFilter;
    }*/
}
