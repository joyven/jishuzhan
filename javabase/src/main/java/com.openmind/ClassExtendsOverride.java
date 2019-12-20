package com.openmind;

/**
 * jishuzhan
 * 在Java中，变量不能被重写
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 09:40
 * @desc
 */
class ClassExtendsOverride {
    public int x = 0;
    public static int y = 0;

    public void m() {
        System.out.println("A");
    }
}

class ClassExtOverrideB extends ClassExtendsOverride {
    public int x = 1;
    public static int y =2;

    @Override
    public void m() {
        System.out.println("B");
    }

    public static void main(String[] args) {
        ClassExtendsOverride a = new ClassExtOverrideB();
        System.out.println(a.x);  // 0
        System.out.println(a.y);  // 0
        a.m();    // B
    }
}
