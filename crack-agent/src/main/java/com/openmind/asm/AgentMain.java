package com.openmind.asm;

import java.lang.instrument.Instrumentation;

/**
 * AgentMain
 *
 * @author zhoujunwen
 * @date 2020-09-24 11:13
 * @desc
 */
public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new MyClassFileTransformer());
    }
}
