package com.openmind.inner;

/**
 * jishuzhan
 *
 * 成员内部类可直接访问外部类（使用：外部类.this.xxx）；
 * 外部成员类要访问内部类，必须先建立成员内部类对象；
 * 成员内部类可使用任意作用域修饰（public、protected、默认、private）；
 * 成员内部类可访问外部类任何作用域修饰的属性和方法；
 * 外部类建立成员内部类对象之后，可以访问任何作用域修饰的内部类属性和方法。
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 10:28
 * @desc
 */
class MemberOuterClass {
    private String a = "I'm a outer class attribute";
    public void sayHi() {
        System.out.println("Hi~,I'm a outer class");
    }

    public void callInner() {
        InnerClass inner = new InnerClass();
        inner.sayHi();
    }

    class InnerClass {
        private void sayHi() {
            MemberOuterClass.this.sayHi();
            System.out.println(MemberOuterClass.this.a);
            System.out.println("Hi, this inner class");
        }

        void sayHello() {
            MemberOuterClass.this.sayHi();
            System.out.println(MemberOuterClass.this.a);
            System.out.println("Hello, this inner class");
        }
    }
}

class MemberOuterClassTest {
    public static void main(String[] args) {
        MemberOuterClass outer = new MemberOuterClass();
        outer.sayHi();
        outer.callInner();

        MemberOuterClass.InnerClass inner =new MemberOuterClass().new InnerClass();
        inner.sayHello();
    }
}


