package com.openmind.auth;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 当走完FilterInvocationSecurityMetadataSource的getAttributes方法后，则会进入到
 * AccessDecisionManager类中进行角色信息对比。
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 22:04
 * @desc
 */
//@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    /**
     * @param auth             当前用户登录的信息
     * @param object           FilterInvocation对象，可以取货当前请求对象等
     * @param configAttributes FilterInvocationSecurityMetadataSource中的getAttributes方法的返回值，即当前请求URL所需要的角色
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication auth, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (ConfigAttribute ca : configAttributes) {
            if ("ROLE_LOGIN".equalsIgnoreCase(ca.getAttribute())
                    && auth instanceof UsernamePasswordAuthenticationToken) {
                return;
            }
            for (GrantedAuthority authority : authorities) {
                if (ca.getAttribute().equalsIgnoreCase(authority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
