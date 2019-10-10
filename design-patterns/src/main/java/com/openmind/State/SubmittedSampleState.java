package com.openmind.State;

/**
 * 提交抽检任务时，状态属于已提交，要实现具体的<code>submitSampleTask</code>方法
 * 在待抽检状态下，接下来的动作，应该只能是开始抽检或者提交任务。
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 11:28
 * @desc
 */
public class SubmittedSampleState extends SampleState {
    @Override
    public void createSampleTask() {
        // do nothing
    }

    @Override
    public void startSampleTask() {
        // do nothing
    }

    @Override
    public boolean isCompleteSampled() {
        // do nothing
        return true;
    }

    @Override
    public void submitSampleTask() {
        // TODO 2019年10月08日11:47:48
    }
}
