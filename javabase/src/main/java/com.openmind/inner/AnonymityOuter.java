package com.openmind.inner;

/**
 * jishuzhan
 *
 * 匿名内部类必须继承一个父类或者实现一个接口
 * 匿名内部类不能定义任何静态成员和方法
 * 匿名内部类中的方法不能是抽象的
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 11:21
 * @desc
 */
interface AnonymityOuter {
    void sayHi();
}


class AnonymityOuterTest {
    public static void main(String[] args) {
        /*AnonymityOuter anonymityOuter = new AnonymityOuter() {
            @Override
            public void sayHi() {
                System.out.println("Hi, AnonymityOuter.");
            }
        };*/
        AnonymityOuter anonymityOuter = () -> System.out.println("Hi, AnonymityOuter.");
        anonymityOuter.sayHi();
    }
}