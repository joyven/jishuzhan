package com.openmind.shiro;

import com.openmind.dao.MenuMapper;
import com.openmind.dao.RoleMapper;
import com.openmind.dao.UserMapper;
import com.openmind.vo.Menu;
import com.openmind.vo.Role;
import com.openmind.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UserRealm
 *
 * @author zhoujunwen
 * @date 2020-01-13
 * @time 23:25
 * @desc
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    MenuMapper menuMapper;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthenticationException("PrincipalCollection method argument cannot be null.");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());

        List<Role> roles = roleMapper.getUserRoleByUid(user.getId());
        int[] rids = roles.stream().mapToInt(Role::getId).toArray();
        Arrays.sort(rids);
        Set<String> permissions = new HashSet<>();
        if (!CollectionUtils.isEmpty(roles)) {
            List<Menu> menus = menuMapper.getAllMenuWithRoles();
            for (Menu menu : menus) {
                for (Role r : menu.getRoles()) {
                    if (Arrays.binarySearch(rids, r.getId()) != -1) {
                        permissions.add(menu.getPattern());
                        break;
                    }
                }
            }
        }
        info.setStringPermissions(permissions);
        // 为了和spring security同用一张表，因此shiro需要处理角色，去掉前缀 ROLE_
        info.setRoles(roles.stream().map(r -> StringUtils.replace(r.getName(), "ROLE_", ""))
                .collect(Collectors.toSet()));

        return info;
    }

    /**
     * 认证
     *
     * @param token 登录接口封装的token，这里是 UsernamePasswordToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("未知账号");
        }

        if (!user.isEnabled()) {
            throw new LockedAccountException("账号已锁定");
        }

        /**
         * 三个参数的则是单向hash
         * new SimpleAuthenticationInfo(principal, credentials, realmName);
         *
         *  四个参数，增加了 salt ，防止反推
         *  principal：认证的实体信息，可以是username，也可以是数据库表对应的用户的实体对象
         *  credentials：数据库中的密码（经过加密的密码）
         *  credentialsSalt：盐值（使用用户名）
         *  realmName：当前realm对象的name，调用父类的getName()方法即可
         */
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(username),
                getName());

        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("session", user);
        session.setAttribute("session-id", user.getId());

        return info;
    }
}
