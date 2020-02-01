package com.openmind.vo;

import lombok.Data;

import java.util.List;

/**
 * Menu
 *
 * @author zhoujunwen
 * @date 2020-01-08
 * @time 21:11
 * @desc
 */
@Data
public class Menu {
    private Integer id;
    private String pattern;
    List<Role> roles;
}
