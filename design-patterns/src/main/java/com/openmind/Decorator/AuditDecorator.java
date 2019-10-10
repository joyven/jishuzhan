package com.openmind.Decorator;

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
    }

    // 审核员的其他操作
    public void doSomething() {

    }
}
