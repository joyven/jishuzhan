//package com.openmind.asm;
//
//import jdk.internal.org.objectweb.asm.*;
//
///**
// * MyClassVistor
// *
// * @author zhoujunwen
// * @date 2020-09-24 10:08
// * @desc
// */
//public class MyClassVistor extends ClassVisitor {
//    public MyClassVistor(int i) {
//        super(i);
//    }
//
//    public MyClassVistor(int i, ClassVisitor classVisitor) {
//        super(i, classVisitor);
//    }
//
//    @Override
//    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//        if ("canLoad".equals(name)) {
//            return new MyMethodVistor(Opcodes.ASM5, mv, access, name, desc);
//        }
//        return mv;
//    }
//}
