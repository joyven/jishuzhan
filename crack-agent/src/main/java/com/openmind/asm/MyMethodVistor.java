package com.openmind.asm;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * MyMethodVistor
 *
 * @author zhoujunwen
 * @date 2020-09-24 09:54
 * @desc
 */
public class MyMethodVistor extends AdviceAdapter {

    public MyMethodVistor(MethodVisitor methodVisitor, int i, String s, String s1) {
        super(methodVisitor, i, s, s1);
    }

    @Override
    protected void onMethodEnter() {
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
    }
}
