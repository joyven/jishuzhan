package com.openmind.dao;

import com.openmind.vo.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MenuMapper
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 21:08
 * @desc
 */
public interface MenuMapper {
    @Results(id = "roleMenuMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "pattern", property = "pattern"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.openmind.dao.RoleMapper.getAllRolesByMenuId"))

    })
    @Select("SELECT * from menu")
    @ResultType(Menu.class)
    List<Menu> getAllMenuWithRoles();


}
