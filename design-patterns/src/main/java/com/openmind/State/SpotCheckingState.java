package com.openmind.State;

/**
 * 抽检任务时，状态属于抽检中，要实现具体的<code>startSampleTask</code>方法
 * 在抽检中的状态下，接下来的动作，应该需要校验是否完成。
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 10:42
 * @desc
 */
public class SpotCheckingState extends SampleState {

    /**
     * 在抽检状态下，不能创建任务
     */
    @Override
    public void createSampleTask() {
        // do nothing
    }

    /**
     * 开始真正抽检任务
     */
    @Override
    public void startSampleTask() {
        // 抽检任务 TODO
    }

    /**
     * 抽检任务中，唯一能做的就是校验是否抽检完成
     */
    @Override
    public boolean isCompleteSampled() {
        // 设置抽检状态
        sampleTaskContent.setSampleState(SampleTaskContent.COMPLETE_SAMPLED);
        // 委托动作
        sampleTaskContent.getSampleState().isCompleteSampled();

        return true;
    }

    @Override
    public void submitSampleTask() {
        // do nothing
    }
}
