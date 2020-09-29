package com.openmind.decorator;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 15:15
 * @desc
 */
public class Decorator implements ILabel  {

    protected ILabel iLabel;

    public Decorator(ILabel iLabel) {
        this.iLabel = iLabel;
    }

    @Override
    public void label() {
        // 实现自己的标注逻辑
        iLabel.label();
    }
}
