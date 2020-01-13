package com.openmind.auth;

import com.openmind.dao.MenuMapper;
import com.openmind.vo.Menu;
import com.openmind.vo.Role;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HttpSecurity 配置的认证授权不够灵活，无线实现资源和角色的动态调整，要实现动态配置URL权限，就需要开发者
 * 自定义权限配置。
 * - 首先实现FilterInvocationSecurityMetadataSource
 * - 自定义AccessDecisionManager
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 21:05
 * @desc
 */
//@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    /**
     * 匹配ant风格的URL资源
     */
    AntPathMatcher matcher = new AntPathMatcher();

    @Resource
    MenuMapper menuMapper;

    /**
     * 实现getAttributes方法，参数是FilterInvocation，可以从找那个提取当前请求的URL
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> allMenus = menuMapper.getAllMenuWithRoles();
        for (Menu menu : allMenus) {
            if (matcher.match(menu.getPattern(), requestUrl)) {
                List<Role> roles = menu.getRoles();
                // 提取数据库中当前菜单对应的角色资源
                Set<String> roleSet = Optional.of(roles).orElse(new ArrayList<>())
                        .stream().map(Role::getName).collect(Collectors.toSet());
                String[] roleArr = roleSet.toArray(new String[roleSet.size()]);

                return SecurityConfig.createList(roleArr);

            }
        }
        // 返回登录
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    /**
     * 该方法用来返回所有定义好的权限资源，Spring Security在启动时会消炎相关配置是否正确，如果不需要校验，那么爱方法直接返回null即可。
     *
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 返回类对象是否支持校验
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
