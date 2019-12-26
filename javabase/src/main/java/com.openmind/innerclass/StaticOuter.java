package com.openmind.innerclass;

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
class StaticOuter {
    String name = "sss";
    public StaticOuter() {
        System.out.println("This a outer class");
    }

    protected static class InnerClass {
        public void sayHi() {
            System.out.println("Hi,InnerClass");
            System.out.println(new StaticOuter().name);
//            System.out.println(StaticOuter.this.name); // error
        }
    }
}

class StaticOuterTest {
    public static void main(String[] args) {
        StaticOuter.InnerClass inner = new StaticOuter.InnerClass();
        inner.sayHi();
    }
}

