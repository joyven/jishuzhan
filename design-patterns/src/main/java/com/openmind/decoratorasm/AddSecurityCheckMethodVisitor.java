package com.openmind.decoratorasm;

import org.objectweb.asm.*;

/**
 * AddSecurityCheckMethodVisitor
 *
 * @author zhoujunwen
 * @date 2020-09-28 18:04
 * @desc
 */
public class AddSecurityCheckMethodVisitor extends MethodAdapter {
    public AddSecurityCheckMethodVisitor(MethodVisitor methodVisitor) {
        super(methodVisitor);
    }

    @Override
    public void visitCode() {
        visitMethodInsn(Opcodes.INVOKESTATIC, "com.openmind.decoratorasm.SecurityChecker", "checkSecurity", "()V");
    }
}
