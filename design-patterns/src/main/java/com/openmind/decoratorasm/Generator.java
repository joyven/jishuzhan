package com.openmind.decoratorasm;

import org.objectweb.asm.*;

import java.io.*;

/**
 * Generator
 *
 * @author zhoujunwen
 * @date 2020-09-28 18:07
 * @desc
 */
public class Generator {
    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader("com.openmind.decoratorasm.Account");
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter ca = new AddSecurityCheckClassVisitor(cw);
        cr.accept(ca, ClassReader.SKIP_DEBUG);
        byte[] data = cw.toByteArray();
        File file = new File("Account.class");
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();

    }
}
