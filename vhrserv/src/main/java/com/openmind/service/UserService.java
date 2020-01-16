package com.openmind.service;

import com.openmind.dao.RoleMapper;
import com.openmind.dao.UserMapper;
import com.openmind.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService实现UserDetailsService接口，并实现loadUserByUsername方法，该方法的参数就是用户登录时输入的用户名，
 * 通过用户名去数据库中查找用户。当获取到user对象返回后，交由DaoAuthenticationProvider类去对比密码是否正确
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 12:10
 * @desc
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;

    /**
     * 用户登录时自动调用
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        user.setRoles(roleMapper.getUserRoleByUid(user.getId()));
        return user;
    }
}
