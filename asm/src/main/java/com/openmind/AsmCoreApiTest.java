package com.openmind;

import org.objectweb.asm.*;

import java.io.IOException;

/**
 * AsmCoreApiTest
 *
 * @author zhoujunwen
 * @date 2020-09-29 11:51
 * @desc
 */
public class AsmCoreApiTest {
    public void test() throws IOException {
        ClassReader cr = new ClassReader("com.openmind.AppMain");
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new MyClassVisitor(cw);
        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
        cw.toByteArray();
    }

    static class MyClassVisitor extends ClassAdapter {

        public MyClassVisitor(ClassVisitor cv) {
            super(cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            System.out.println("visit:" + name);
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public void visitSource(String source, String debug) {
            super.visitSource(source, debug);
        }

        @Override
        public void visitOuterClass(String owner, String name, String desc) {
            System.out.println("visitOuterClass " + owner + "  " + name + " " + desc);
            super.visitOuterClass(owner, name, desc);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return super.visitAnnotation(desc, visible);
        }

        @Override
        public void visitAttribute(Attribute attr) {
            super.visitAttribute(attr);
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {
            System.out.println("visitInnerClass" + name + "  " + outerName + " " + innerName);
            super.visitInnerClass(name, outerName, innerName, access);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            System.out.println("field: " + name);
            return super.visitField(access, name, desc, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println("method: " + name);
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            System.out.println("visitEnd");
            super.visitEnd();
        }
    }

    public static void main(String[] args) throws IOException {
        AsmCoreApiTest asmCoreApiTest = new AsmCoreApiTest();
        asmCoreApiTest.test();
    }
}



