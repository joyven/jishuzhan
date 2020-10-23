package com.openmind;


import org.apache.commons.io.FileUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * AddFeildTest
 *
 * @author zhoujunwen
 * @date 2020-09-29 14:17
 * @desc
 */
public class AddFeildTest {
    public void coreApiAddField() throws IOException {
        //  前提是使用 javac 或者 mvn clean package 编译 AppMain 类
        URL url = AddFeildTest.class.getClassLoader().getResource("com/openmind/AppMain.class");
        byte[] bytes = FileUtils.readFileToByteArray(new File(url.getFile()));
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassAdapter(cw) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
                if (fv != null) {
                    fv.visitEnd();
                }
            }
        };

        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
        byte[] byteModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile(new File(url.getFile().substring(0, url.getFile().length() - 6) + "$Proxy.class"), byteModified);
    }

    public void treeApiAddField() throws IOException {
        //  前提是使用 javac 或者 mvn clean package 编译 AppMain 类
        URL url = AddFeildTest.class.getClassLoader().getResource("com/openmind/AppMain.class");
        byte[] bytes = FileUtils.readFileToByteArray(new File(url.getFile()));
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);

        FieldNode fn = new FieldNode(Opcodes.ACC_PUBLIC, "xyz", "Ljava/lang/String;", null, null);
        cn.fields.add(fn);

        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);

        byte[] byteModified = cw.toByteArray();
        FileUtils.writeByteArrayToFile(new File(url.getFile().substring(0, url.getFile().length() - 6) + "$Proxy2.class"), byteModified);
    }
    
    public static void main(String[] args) throws IOException {
        AddFeildTest test = new AddFeildTest();
        test.coreApiAddField();
        test.treeApiAddField();
    }
}
