package com.openmind.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * MethodService
 * 使用@Secured、@PreAuthorize以及@PostAuthorize注解需要先
 * 开启@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)这个注解
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 11:12
 * @desc
 */
@Service
public class MethodService {
    /**
     * @return
     */
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "hello admin";
    }

    /**
     * @return
     * @PreAuthorize 该注解在方法前进行验证
     */
    @PreAuthorize("hasRole('ADMIN') and hasRole('DBA')")
    public String dba() {
        return "hello dba";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DBA', 'USER')")
    public String user() {
        return "hello user";
    }
}
