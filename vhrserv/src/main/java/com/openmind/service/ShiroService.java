package com.openmind.service;

import com.openmind.dao.MenuMapper;
import com.openmind.vo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ShiroService
 *
 * @author zhoujunwen
 * @date 2020-01-14
 * @time 14:29
 * @desc
 */
//@Service
public class ShiroService implements ShiroServiceInter {
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> selectAll() {
        return menuMapper.getAllMenuWithRoles();
    }


//    @Resource
//    ShiroFilterFactoryBean shiroFilterFactoryBean;

    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");

        /*List<Menu> menus = menuMapper.getAllMenuWithRoles();
        for (Menu menu : menus) {
            List<Role> roles = menu.getRoles();
            Set<String> roleNames = roles.stream()
                    .map(r -> StringUtils.replace(r.getName(), "ROLE_", ""))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(roleNames)) {
                filterChainDefinitionMap.put(menu.getPattern(), "authc, roles[" + StringUtils.join(roleNames, ",") + "]");
                // String permission = "perms[" + resources.getResurl()+ "]";
                //                filterChainDefinitionMap.put(resources.getResurl(),permission);
            }
            // 如果有资源code，还可以加入资源code
            // filterChainDefinitionMap.put(menu.getPattern(), "authc, perms[document:read]");
        }*/
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {
        /*if (shiroFilterFactoryBean != null) {
            synchronized (shiroFilterFactoryBean) {

                AbstractShiroFilter shiroFilter;
                try {
                    shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                } catch (Exception e) {
                    throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
                }

                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                        .getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

                // 清空老的权限控制
                manager.getFilterChains().clear();

                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
                shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
                // 重新构建生成
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                for (Map.Entry<String, String> entry : chains.entrySet()) {
                    String url = entry.getKey();
                    String chainDefinition = entry.getValue().trim().replace(" ", "");
                    manager.createChain(url, chainDefinition);
                }

                System.out.println("更新权限成功！！");
            }
        }*/
    }
}
