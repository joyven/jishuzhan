package com.openmind.decorator;

import org.junit.Before;
import org.junit.Test;

/**
 * LabelTest
 *
 * @author zhoujunwen
 * @date 2020-09-28 17:15
 * @desc
 */
public class LabelTest {
    private ILabel iLabel;

    @Before
    public void before() {
        iLabel = new Label();
    }

    @Test
    public void testLabel() {
        LabelDecorator labelDecorator = new LabelDecorator(iLabel);
        labelDecorator.label();
        System.out.println("------------------------");
    }

    @Test
    public void testAudit() {
        AuditDecorator auditDecorator = new AuditDecorator(iLabel);
        auditDecorator.label();
        System.out.println("------------------------");
    }

    @Test
    public void testSample() {
        SampleDecorator sampleDecorator = new SampleDecorator(iLabel);
        sampleDecorator.label();
        System.out.println("------------------------");
    }
}
