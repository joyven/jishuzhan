package com.openmind.decorator;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 15:14
 * @desc
 */
public class Label implements ILabel {
    @Override
    public void label() {
        System.out.println("此处是打标相同的动作");
    }
}
