package com.openmind.decorator;

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
        System.out.println("标注师打标开始");
        iLabel.label();
        System.out.println("标注师达标结束");
    }

    // 标注师的其他操作
    public void doSomething() {

    }
}
