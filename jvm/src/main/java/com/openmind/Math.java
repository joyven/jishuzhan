package com.openmind;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-17
 * @time 20:26
 * @desc
 */
public class Math {
    public static int initData = 666;
    public static User user = new User();

    public int compute() {
        int a =1;
        int b = 2;
        int c = (a+b) * 10;
        return c;
    }

    public static void main(String[] args) {
        Math math = new Math();
        int age = Math.user.getAge();
        System.out.println(age);

        int res = math.compute();
        System.out.println(res);

        System.out.println("1" + "2" + 1 + 2);
    }
}
