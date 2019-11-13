package com;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-12
 * @time 15:02
 * @desc
 */
public class supperTest {
    class supper {
        String name;

        public supper(String name) {
            this.name = name;
        }
        public void func1() {
            System.out.println("supper " + name);
        }
    }

    class sub extends supper {
        public sub(String name) {
            super(name);
        }

        public void func1() {
            System.out.println("supper " + name);
        }
    }
}
