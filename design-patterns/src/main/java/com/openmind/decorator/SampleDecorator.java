package com.openmind.decorator;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 15:22
 * @desc
 */
public class SampleDecorator extends Decorator {
    public SampleDecorator(ILabel iLabel) {
        super(iLabel);
    }

    @Override
    public void label() {
        // 抽检员标注的方式
        System.out.println("抽检员抽检标注开始");
        iLabel.label();
        System.out.println("抽检员抽检标注结束");

    }

    // 标抽检员的其他操作
    public void doSomething() {
        // TODO: 2019-10-08 其他操作
    }
}
