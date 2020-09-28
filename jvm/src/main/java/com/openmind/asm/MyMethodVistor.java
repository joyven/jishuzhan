//package com.openmind.asm;
//
//
//import jdk.internal.org.objectweb.asm.MethodVisitor;
//import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;
//
///**
// * MyMethodVistor
// *
// * @author zhoujunwen
// * @date 2020-09-24 09:54
// * @desc
// */
//public class MyMethodVistor extends AdviceAdapter {
//    protected MyMethodVistor(int i, MethodVisitor methodVisitor, int i1, String s, String s1) {
//        super(i, methodVisitor, i1, s, s1);
//    }
//
//    @Override
//    protected void onMethodEnter() {
//        mv.visitInsn(ICONST_1);
//        mv.visitInsn(IRETURN);
//    }
//}
