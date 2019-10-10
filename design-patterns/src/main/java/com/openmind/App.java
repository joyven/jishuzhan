package com.openmind;

import com.openmind.State.PendingInspectionState;
import com.openmind.State.SampleTaskContent;

import com.openmind.Decorator.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SampleTaskContent content = new SampleTaskContent();
        content.setSampleState(new PendingInspectionState());

        content.startSpotCheckTask();
        content.submitSampleTask();
        content.isCompleteSampled();

        Label label = new Label();
        // 创建标注阶段的标注装饰器
        ILabel iLabel = new LabelDecorator(label);
        // 创建审核阶段的装饰器
        iLabel = new AuditDecorator(label);
        // 创建抽检阶段的装饰器
        iLabel = new SampleDecorator(label);

        iLabel.label();

    }
}
