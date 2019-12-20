package com.openmind.inner;

/**
 * jishuzhan
 *
 * 不能从静态成员内部类中访问非静态外部类对象
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 11:01
 * @desc
 */
class StaticOuterClass {
    public StaticOuterClass() {
        System.out.println("This a outer class");
    }

    protected static class InnerClass {
        public void sayHi() {
            System.out.println("Hi,InnerClass");
        }
    }
}

class StaticOuterClassTest {
    public static void main(String[] args) {
        StaticOuterClass.InnerClass inner = new StaticOuterClass.InnerClass();
        inner.sayHi();
    }
}

