package com.openmind.decorator;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 15:21
 * @desc
 */
public class AuditDecorator extends Decorator {

    public AuditDecorator(ILabel iLabel) {
        super(iLabel);
    }
    @Override
    public void label() {
        // 审核员标注的方式
        System.out.println("审核员审核打标开始");
        iLabel.label();
        System.out.println("审核员审核打标结束");
    }

    // 审核员的其他操作
    public void doSomething() {

    }
}
