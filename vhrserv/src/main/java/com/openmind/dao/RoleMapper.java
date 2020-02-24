package com.openmind.dao;

import com.openmind.vo.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * RoleMapper
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 21:40
 * @desc
 */
//@Mapper
public interface RoleMapper {
    @Results(id = "roleMenuMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "name", property = "name"),
            @Result(column = "nameZh", property = "nameZh")

    })
    @Select("SELECT r.* FROM role r LEFT JOIN menu_role mr ON r.id = mr.rid where mr.mid = #{mid}")
    @ResultType(Role.class)
    List<Role> getAllRolesByMenuId(@Param("mid") Integer mid);

    /**
     * 根据用户id查询角色
     *
     * @param id
     * @return
     */
    @Select("select * from role r,user_role ur where r.id=ur.rid and ur.uid=#{id}")
    List<Role> getUserRoleByUid(@Param("id") Integer id);
}
