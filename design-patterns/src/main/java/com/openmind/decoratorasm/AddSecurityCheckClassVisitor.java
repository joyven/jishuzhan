package com.openmind.decoratorasm;

import org.objectweb.asm.*;

/**
 * AddSecurityCheckClassVisitor
 *
 * @author zhoujunwen
 * @date 2020-09-28 18:00
 * @desc
 */
public class AddSecurityCheckClassVisitor extends ClassAdapter {
    public AddSecurityCheckClassVisitor(ClassVisitor classVisitor) {
        super(classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        MethodVisitor mv = cv.visitMethod(i, s, s1, s2, strings);
        if (mv != null) {
            if (s.equals("operator")) {
                mv = new AddSecurityCheckMethodVisitor(mv);
            }
        }
        return mv;
    }
}
