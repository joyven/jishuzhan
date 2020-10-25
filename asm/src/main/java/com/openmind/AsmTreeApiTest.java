package com.openmind;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.List;

/**
 * AsmTreeApiTest
 *
 * @author zhoujunwen
 * @date 2020-09-29 14:00
 * @desc
 */
public class AsmTreeApiTest {
    public void test() throws IOException {
        ClassReader cr = new ClassReader("com.openmind.AppMain");
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_CODE| ClassReader.SKIP_DEBUG);

        List<FieldNode> fieldNodes = cn.fields;
        for (int i = 0; i < fieldNodes.size(); i++) {
            FieldNode fieldNode = fieldNodes.get(i);
            System.out.println("field: " + fieldNode.name);
        }

        List<MethodNode> methodNodes = cn.methods;
        for (int i = 0; i < methodNodes.size(); i++) {
            MethodNode methodNode = methodNodes.get(i);
            System.out.println("method: " +methodNode.name);
        }

        ClassWriter cw = new ClassWriter(0);
        cr.accept(cn, 0);
        byte[] bytes = cw.toByteArray();
        System.out.println(bytes.length);
    }

    public static void main(String[] args) throws IOException {
        AsmTreeApiTest asmTreeApiTest = new AsmTreeApiTest();
        asmTreeApiTest.test();
    }
}
