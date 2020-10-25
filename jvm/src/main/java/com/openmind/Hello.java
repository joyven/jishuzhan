package com.openmind;

/**
 * Hello
 *
 * @author zhoujunwen
 * @date 2020-09-15 13:44
 * @desc
 */
public class Hello {
    public static final int DEFAULT_SIZE = Integer.MAX_VALUE;
    public static final long DEFAULT_MAX_LONG = Long.MAX_VALUE;
    public static final long DEFAULT_MAX_LONG2 = 0x7fffffffffffffffL;
    public static final String ddd = "xxxx";
    public static final double dd = Double.POSITIVE_INFINITY;
    public static final double dd2 = Double.longBitsToDouble(0x7ff0000000000000L);


    private Hello() {
    }

    public static void main(String[] args) {
        int s = 0;
        int b = 1;
        s = s + b + DEFAULT_SIZE;
        System.out.println(s);
        System.out.printf("%010x\n", DEFAULT_MAX_LONG);
        boolean c = DEFAULT_MAX_LONG2 == DEFAULT_MAX_LONG;
        System.out.println(c);
        System.out.println(dd == dd2);
        return;
    }
}
