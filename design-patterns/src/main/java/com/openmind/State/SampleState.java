package com.openmind.State;

import lombok.Getter;
import lombok.Setter;

/**
 * 抽检状态抽象类
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 10:52
 * @desc
 */
public abstract class SampleState {

    // 上下文，接收状态的变化
    @Setter
    @Getter
    protected SampleTaskContent sampleTaskContent;

    /**
     * 创建抽检任务
     */
    public abstract void createSampleTask();

    /**
     * 抽检任务
     */
    public abstract void startSampleTask();

    /**
     * 检验抽检是否完成，如果完成，返回true，否则返回false
     * @return
     */
    public abstract boolean isCompleteSampled();

    /**
     * 抽检任务已提交
     */
    public abstract void submitSampleTask();
}
