package com.openmind.State;

import lombok.Getter;

/**
 * 抽检任务上下文环境
 *
 * @author zhoujunwen
 * @date 2019-10-08
 * @time 10:35
 * @desc
 */
public class SampleTaskContent {
    public static SampleState PENDING_INSPECTION = new PendingInspectionState();
    public static SampleState SPOT_CHECK = new SpotCheckingState();
    public static SampleState COMPLETE_SAMPLED = new CompleteSampledState();
    public static SampleState Submitted_SAMPLED = new SubmittedSampleState();
    /**
     * 抽样任务状态
     */
    @Getter
    private SampleState sampleState;

    public void setSampleState(SampleState sampleState) {
        this.sampleState = sampleState;
        this.sampleState.setSampleTaskContent(this);
    }

    public void createSampleTask() {
        this.sampleState.createSampleTask();
    }

    public void startSpotCheckTask() {
        this.sampleState.startSampleTask();
    }

    public void isCompleteSampled() {
        this.sampleState.isCompleteSampled();
    }

    public void submitSampleTask() {
        this.sampleState.submitSampleTask();
    }
}
