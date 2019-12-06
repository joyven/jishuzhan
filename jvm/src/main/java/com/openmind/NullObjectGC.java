package com.openmind;

/**
 * jishuzhan
 *
 *  运行代码时，在IDEA的VM Options中增加打印GC日志的参数：-XX:+PrintGC 或者 -XX:+PrintGCDetails
 *  需要记住的一个分析命令： <code>javap -l -c NullObjectGC</code>或者<code>javap -v -c NullObjectGC</code>
 *  在编译后的class文件目录下执行，可以查看本地变量表LocalVariableTable。
 *
 *
 *                      ¦ˉˉˉˉˉˉˉˉˉˉ¦
 *                      ¦ GC ROOTS ¦
 *                      ¦__________¦
 *                            |        可达性分析算法           GC Root Set
 *   =====================================================================
 *                            |
 *                            ↓
 *                       ╭--------◑╮                   ╭--------▒╮
 *                       ┆ object2 ┆                   ┆ object5 ┆
 *                       ╰---------╯                   ╰---------╯
 *                         ╱    ╲                         ╱    ╲
 *                        ╱      ╲                       ╱      ╲
 *                       ↙        ↘                     ↙        ↘
 *                 ╭--------◑╮   ╭--------◑╮      ╭--------▒╮   ╭--------▒╮
 *                 ┆ object2 ┆   ┆ object3 ┆      ┆ object6 ┆   ┆ object7 ┆
 *                 ╰---------╯   ╰---------╯      ╰---------╯   ╰---------╯
 *                      |
 *                      ↓
 *                 ╭--------◑╮              ╭-------◑╮             ╭-------▒╮
 *                 ┆ object4 ┆   alive obj: ┆ object ┆   dead obj: ┆ object ┆
 *                 ╰---------╯              ╰--------╯             ╰--------╯
 *
 *
 * @author zhoujunwen
 * @date 2019-12-06
 * @time 10:01
 * @desc
 */
public class NullObjectGC {

    /**
     *
     * 虽然bytes是一个局部变量，且在if块内使用，但执行System.gc()之后，从日志看出，执行了FULL GC，
     * 但是细看GC日志，就会发现：[ParOldGen: 1048584K->1049704K(1224192K)] ，
     * 这意味着在执行了FULL GC之后，bytes对象并没有回收。
     *
     * [GC (System.gc()) [PSYoungGen: 6559K->1204K(76288K)] 1055135K->1049788K(1300480K), 0.0208279 secs] [Times: user=0.06 sys=0.00, real=0.02 secs]
     * [Full GC (System.gc()) [PSYoungGen: 1204K->0K(76288K)] [ParOldGen: 1048584K->1049704K(1224192K)] 1049788K->1049704K(1300480K), [Metaspace: 3151K->3151K(1056768K)], 0.0220738 secs] [Times: user=0.02 sys=0.01, real=0.03 secs]
     * Heap
     *  PSYoungGen      total 76288K, used 655K [0x000000076ab00000, 0x0000000770000000, 0x00000007c0000000)
     *   eden space 65536K, 1% used [0x000000076ab00000,0x000000076aba3ee8,0x000000076eb00000)
     *   from space 10752K, 0% used [0x000000076eb00000,0x000000076eb00000,0x000000076f580000)
     *   to   space 10752K, 0% used [0x000000076f580000,0x000000076f580000,0x0000000770000000)
     *  ParOldGen       total 1224192K, used 1049704K [0x00000006c0000000, 0x000000070ab80000, 0x000000076ab00000)
     *   object space 1224192K, 85% used [0x00000006c0000000,0x000000070011a048,0x000000070ab80000)
     *  Metaspace       used 3160K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 341K, capacity 388K, committed 512K, reserved 1048576K
     *
     * @param args
     */
    /*public static void main(String[] args) {
        if (true) {
            byte[] bytes = new byte[1024 * 1024 * 1024];
            System.out.println(bytes.length / 1024);
        }
        System.gc();
    }*/

    /**
     * 就上面的代码做了修改，在①的地方增加了将bytes对象设置为null的操作，
     * 此时再看GC日志，老年代ParOldGen从1048584K回收后到1129K，回收了1224192K。
     * 说明对象赋值为null后，GC会将其回收。
     *
     * 下面是GC日志：
     *
     * [GC (System.gc()) [PSYoungGen: 9180K->1252K(76288K)] 1057756K->1049836K(1300480K), 0.0079791 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
     * [Full GC (System.gc()) [PSYoungGen: 1252K->0K(76288K)] [ParOldGen: 1048584K->1129K(1224192K)] 1049836K->1129K(1300480K),
     * [Metaspace: 3162K->3162K(1056768K)], 0.1041456 secs] [Times: user=0.02 sys=0.02, real=0.11 secs]
     * Heap
     *  PSYoungGen      total 76288K, used 1752K [0x000000076ab00000, 0x0000000770000000, 0x00000007c0000000)
     *   eden space 65536K, 2% used [0x000000076ab00000,0x000000076acb6008,0x000000076eb00000)
     *   from space 10752K, 0% used [0x000000076eb00000,0x000000076eb00000,0x000000076f580000)
     *   to   space 10752K, 0% used [0x000000076f580000,0x000000076f580000,0x0000000770000000)
     *  ParOldGen       total 1224192K, used 1129K [0x00000006c0000000, 0x000000070ab80000, 0x000000076ab00000)
     *   object space 1224192K, 0% used [0x00000006c0000000,0x00000006c011a600,0x000000070ab80000)
     *  Metaspace       used 3207K, capacity 4496K, committed 4864K, reserved 1056768K
     *   class space    used 345K, capacity 388K, committed 512K, reserved 1048576K
     *
     *  下面是javap反编译后查看的局部变量表信息：
     *
     *  LocalVariableTable:
     *       Start  Length  Slot  Name   Signature
     *           5      14     1 bytes   [B
     *           0      23     0  args   [Ljava/lang/String;
     *
     * @param args
     */
    public static void main(String[] args) {
        if (true) {
            byte[] bytes = new byte[1024 * 1024 * 1024];
            System.out.println(bytes.length / 1024);
            // ① 将对象设置为null
            bytes = null;
        }
        System.gc();

    }


    /**
     *
     * 再看一个小例子，在②的地方重新定义一个变量newByte，
     * 此时再看GC日志，老年代ParOldGen从1048584K回收后到1128K，回收了1224192K。
     * 说明在局部变量之后对栈重新读写时GC会将不用的对象回收。
     *
     * 下面是GC日志：
     *
     * [GC (System.gc()) [PSYoungGen: 9180K->1236K(76288K)] 1057756K->1049820K(1300480K), 0.0223023 secs] [Times: user=0.07 sys=0.00, real=0.02 secs]
     * [Full GC (System.gc()) [PSYoungGen: 1236K->0K(76288K)] [ParOldGen: 1048584K->1128K(1224192K)] 1049820K->1128K(1300480K),
     * [Metaspace: 3157K->3157K(1056768K)], 0.0424619 secs] [Times: user=0.02 sys=0.01, real=0.04 secs]
     *
     *  JVM的bug：System.gc(); 触发 GC 时，main() 方法的运行时栈中，还存在有对 args 和 bytes 的引用，
     *  GC 判断这两个对象都是存活的，不进行回收。也就是说，代码在离开 if 后，虽然已经离开了 bytes 的作用域，
     *  但在此之后，没有任何对运行时栈的读写，bytes 所在的 slot 还没有被其他变量重用，所以 GC 判断其为存活。
     *
     *  下面是 javap 反编译后查看的局部变量表信息：
     *
     *   LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             5      12     1 bytes   [B
     *             0      23     0  args   [Ljava/lang/String;
     *            19       4     1 newByte   I
     *
     *  从运行时栈中可以看出，加上 int newByte=1 和  bytes = null 效果相同。局部变量表是一组变量值存储空间，用于存放方法参数和方法内定
     *  义的局部变量。局部变量表的容量以变量槽(Variable Slot)为最小单位。参数 args 的 slot 为0，由于 bytes 是 if 内的局部变量，当创建
     *  newByte 时，发现 slot = 1 的槽点中的 bytes 的作用域已经失效，那么就可以复用该 slot，slot的复用导致 bytes 在局部变量表中找不到
     *  引用，因此GC时会将其回收。
     *
     * @param args
     */
    /*public static void main(String[] args) {
        if (true) {
            byte[] bytes = new byte[1024 * 1024 * 1024];
            System.out.println(bytes.length / 1024);
        }
        // ② 重新使用栈
        int newByte = 1;
        System.gc();

    }*/

    // 实验最后，发现此文可完美解释： https://www.jianshu.com/p/a474dd20b08d
}
