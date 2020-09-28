package com.openmind;

/**
 * TryCatchFinallyDemo
 *
 * @author zhoujunwen
 * @date 2020-09-21 10:33
 * @desc
 */
public class TryCatchFinallyDemo {
    public void foo() {
        try {
            tryItOut();
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void tryItOut() {
        System.out.println("sss");
    }

    public void handleException(Exception e) {
    }
}
