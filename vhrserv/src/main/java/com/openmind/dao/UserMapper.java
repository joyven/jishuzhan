package com.openmind.dao;

import com.openmind.vo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * UserMapper
 * 使用@Mapper注解需要在每个mapper上加，还有一种方式，
 * 可以直接在配置文件上加@MapperScan("com.openmind.dao")注解
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 12:13
 * @desc
 */
//@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User getUserByUsername(@Param("username") String username);
}
