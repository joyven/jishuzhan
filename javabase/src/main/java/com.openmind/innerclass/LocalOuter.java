package com.openmind.innerclass;

/**
 * jishuzhan
 *
 * 局部内部类不能使用任何访问修饰符；
 * 局部类如果在方法中，可以直接使用方法中的变量，不需要通过 OutClass.this.xxx 的方式获得。
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 11:13
 * @desc
 */
public class LocalOuter {
    private String age = "1222";
    public void sayHi() {
        String hi = "Hi, I'm a local variable";
        class InnerClass {
            InnerClass(String name) {
                System.out.println("InnerClass:" + name);
            }

            private void sayHi() {
                System.out.println(hi);
                System.out.println(age);
            }
        }
        new InnerClass("Three").sayHi();
        System.out.println("Hi, OutClass");
    }
}

class LocalOuterTest {
    public static void main(String[] args) {
        new LocalOuter().sayHi();
    }
}
