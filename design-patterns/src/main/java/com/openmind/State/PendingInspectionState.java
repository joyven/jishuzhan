package com.openmind.State;

/**
 * 创建抽检任务时，状态属于待抽检，要实现具体的<code>createSampleTask</code>方法
 * 在待抽检状态下，接下来的动作，应该只能是开始抽检。
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 10:38
 * @desc
 */
public class PendingInspectionState extends SampleState {

    /**
     * 实现创建抽检任务的逻辑
     */
    @Override
    public void createSampleTask() {
        // 任务已创建

    }

    // 在待抽检中，唯一能做的事情就是开始抽检
    @Override
    public void startSampleTask() {
        // 状态修改
        sampleTaskContent.setSampleState(SampleTaskContent.SPOT_CHECK);
        // 抽检动作委派
        sampleTaskContent.getSampleState().startSampleTask();
    }

    @Override
    public boolean isCompleteSampled() {
        // do nothing
        return true;
    }

    @Override
    public void submitSampleTask() {
        // do nothing
    }
}
