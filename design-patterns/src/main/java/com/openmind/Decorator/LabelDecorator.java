package com.openmind.Decorator;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 15:17
 * @desc
 */
public class LabelDecorator extends Decorator {
    public LabelDecorator(ILabel iLabel) {
        super(iLabel);
    }

    @Override
    public void label() {
        // 标注师标注的方式
    }

    // 标注师的其他操作
    public void doSomething() {

    }
}
