package com.openmind.asm;


import org.objectweb.asm.*;

/**
 * MyClassVistor
 *
 * @author zhoujunwen
 * @date 2020-09-24 10:08
 * @desc
 */
public class MyClassVistor extends ClassAdapter {
    public MyClassVistor(ClassVisitor classVisitor) {
        super(classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if ("canLoad".equals(name)) {
            return new MyMethodVistor(mv, access, name, desc);
        }
        return mv;
    }

}
