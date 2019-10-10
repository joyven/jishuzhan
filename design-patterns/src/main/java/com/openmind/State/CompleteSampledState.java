package com.openmind.State;

/**
 * 校验抽检任务是否完成时时，状态属于已完成，要实现具体的<code>isCompleteSampled</code>方法
 * 在待抽检状态下，接下来的动作，应该只能是开始抽检或者提交任务。
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 11:28
 * @desc
 */
public class CompleteSampledState extends SampleState {
    @Override
    public void createSampleTask() {
        // do nothing
    }

    @Override
    public void startSampleTask() {
        sampleTaskContent.setSampleState(SampleTaskContent.PENDING_INSPECTION);
        sampleTaskContent.getSampleState().submitSampleTask();
    }

    @Override
    public boolean isCompleteSampled() {
        // TODO: 2019-10-08 具体业务实现
        return true;
    }

    /**
     * 抽检完成状态下，唯一能做的就是提交任务
     */
    @Override
    public void submitSampleTask() {
        sampleTaskContent.setSampleState(SampleTaskContent.Submitted_SAMPLED);
        sampleTaskContent.getSampleState().submitSampleTask();
    }
}
