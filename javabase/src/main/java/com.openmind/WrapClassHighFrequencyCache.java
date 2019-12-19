package com.openmind;


import java.util.HashSet;
import java.util.Set;

/**
 * jishuzhan
 *
 * Integer 在数值区间为 -128~127 时，会直接复用已有对象，在这区间之外的数字才会在堆上产生。
 *
 * 各包装类高频区域的取值范围：
 * Boolean：使用静态 final 定义，就会返回静态值
 * Byte：缓存区 -128~127
 * Short：缓存区 -128~127
 * Character：缓存区 0~127
 * Long：缓存区 -128~127
 * Integer：缓存区 -128~127
 *
 * Float 和 Double 不会有缓存，其他包装类都有缓存。
 * Integer 是唯一一个可以修改缓存范围的包装类，在 VM optons 加入参数: -XX:AutoBoxCacheMax=666 即修改缓存最大值为 666
 *
 * @author zhoujunwen
 * @date 2019-12-10
 * @time 10:29
 * @desc
 */
public class WrapClassHighFrequencyCache {


    public static void main(String[] args) {
        integerCacheRange();
        integerModifyAutoBoxCacheMax();
        wrapClassAutoUnwrap();
        implicitTypeConversion();
        implicitTypeConversion2();
    }

    /**
     * Integer类的高频缓存范围
     */
    public static void integerCacheRange() {

        Integer num1 = 127;
        Integer num2 = 127;
        // true
        System.out.println(num1 == num2);

        Integer num3 = 128;
        Integer num4 = 128;
        // false
        System.out.println(num3 == num4);

        Integer num5 = new Integer(127);
        Integer num6 = new Integer(127);

        // false
        System.out.println(num5 == num6);
        // false
        System.out.println(num1 == num5);

    }


    /**
     *  Integer 是唯一一个可以修改缓存范围的包装类，在 VM optons 加入参数:
     *   -XX:AutoBoxCacheMax=666 即修改缓存最大值为 666
     */
    public static void integerModifyAutoBoxCacheMax() {
        Integer num1 = 128;
        Integer num2 = 128;
        // num1 == num2 is true
        System.out.println("num1 == num2 is " + (num1 == num2));

        Integer num3 = 666;
        Integer num4 = 666;
        // num3 == num4 is true
        System.out.println("num3 == num4 is " + (num3 == num4));
    }

    /**
     * 包装类会自动拆箱
     *
     * 有人认为这和 Integer 高速缓存有关系，但你发现把值改为 10000 结果也是 true,true，
     * 这是因为 Integer 和 int 比较时，会自动拆箱为 int 相当于两个 int 比较，值一定是 true,true。
     */
    public static void wrapClassAutoUnwrap() {
        int i = 100;
        Integer j = new Integer(100);
        // true
        System.out.println(i == j);
        // true
        System.out.println(j.equals(i));
    }


    /**
     * Short 类型 -1 之后转换成了 int 类型，remove() 的时候在集合中找不到 Int 类型的数据，
     * 所以就没有删除任何元素，执行的结果就是 5
     */
    public static void implicitTypeConversion() {
        Set<Short> set = new HashSet<>();
        for (short i = 0; i < 5; i++) {
            set.add(i);
            set.remove(i - 1);
        }
        // 5
        System.out.println(set.size());


        Set<Short> set2 = new HashSet<>();
        for (short i = 0; i < 5; i++) {
            set2.add(i);
            set2.remove((short) (i - 1));
        }
        // 1
        System.out.println(set2.size());
    }

    /**
     * 隐式类型转换，将short+int的结果直接赋值给short时编译会报错
     * 如果用 += 的方式则不会报错
     */
    public static void implicitTypeConversion2() {
        /**
         * 这段代码报错，int不能隐式的转换为short
         * 在1的前面增加(short)进行强制类型你转换依然会有问题
         * 在执行+之前已经隐式的转换为int类型，相加的结果是int，无法将结果隐式的赋值给short类型的s
         */
        /*short s = 2;
        s = s + 1;*/

        /**
         *
         */
        short s0 = 2;
        s0 += 1;
        System.out.println(s0);

        int  i = 0;
        System.out.println(i++);
        System.out.println(++i);
    }
}
