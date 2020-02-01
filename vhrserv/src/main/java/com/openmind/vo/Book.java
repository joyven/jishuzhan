package com.openmind.vo;

import lombok.Data;

/**
 * Book
 *
 * @author zhoujunwen
 * @date 2020-01-06
 * @time 21:59
 * @desc
 */
@Data
public class Book implements Cloneable {
    private Integer id;
    private String name;
    private String author;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
