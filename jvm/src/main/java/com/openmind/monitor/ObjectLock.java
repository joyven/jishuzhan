package com.openmind.monitor;

/**
 * 先生成ObjectLock的class文件，然后进入classes目录下，使用javap进行反编译操作
 * # 进入到 java 文件目录
 * cd /Users/zhoujunwen/Documents/workspace/jishuzhan/jvm/src/main/java/com/openmind/monitor
 * # 编译 ObjectLock.java 文件到-d 指定的目录，编译后会在该目录下生成包括包名转为目录的 class 文件
 * javac -d /Users/zhoujunwen/Documents/workspace/jishuzhan/jvm/target/classes ObjectLock.java
 * # 进入到编译后的 class 文件，其中 com/openmind/monitor 是包名编译产生的目录
 * cd /Users/zhoujunwen/Documents/workspace/jishuzhan/jvm/target/classes/com/openmind/monitor
 * # javap 查看 class 文件内容，-c 分解方法代码，即显示每个方法具体的字节码 -v 打印附加信息
 * javap -c -v ObjectLock
 * <p>
 * 得到结果：
 * <code>
 Classfile /Users/zhoujunwen/Documents/workspace/jishuzhan/jvm/target/classes/com/openmind/monitor/* ObjectLock.class
 *   Last modified 2020-11-30; size 807 bytes
 *   MD5 checksum a3ba96991d2d6cf1c61fa03d4b5880b5
 *   Compiled from "ObjectLock.java"
 * public class com.openmind.monitor.ObjectLock
 *   minor version: 0
 *   major version: 52
 *   flags: ACC_PUBLIC, ACC_SUPER
 * Constant pool:
 *    #1 = Methodref          #2.#28         // java/lang/Object."<init>":()V
 *    #2 = Class              #29            // java/lang/Object
 *    #3 = Fieldref           #14.#30        // com/openmind/monitor/ObjectLock.object:Ljava/lang/Object;
 *    #4 = Fieldref           #31.#32        // java/lang/System.out:Ljava/io/PrintStream;
 *    #5 = String             #33            // hello world
 *    #6 = Methodref          #34.#35        // java/io/PrintStream.println:(Ljava/lang/String;)V
 *    #7 = Methodref          #36.#37        // java/lang/Math.random:()D
 *    #8 = Double             2.0d
 *   #10 = Class              #38            // java/lang/RuntimeException
 *   #11 = Methodref          #10.#28        // java/lang/RuntimeException."<init>":()V
 *   #12 = String             #39            // end
 *   #13 = String             #40            // welcome
 *   #14 = Class              #41            // com/openmind/monitor/ObjectLock
 *   #15 = Utf8               object
 *   #16 = Utf8               Ljava/lang/Object;
 *   #17 = Utf8               <init>
 *   #18 = Utf8               ()V
 *   #19 = Utf8               Code
 *   #20 = Utf8               LineNumberTable
 *   #21 = Utf8               method1
 *   #22 = Utf8               StackMapTable
 *   #23 = Class              #29            // java/lang/Object
 *   #24 = Class              #42            // java/lang/Throwable
 *   #25 = Utf8               method2
 *   #26 = Utf8               SourceFile
 *   #27 = Utf8               ObjectLock.java
 *   #28 = NameAndType        #17:#18        // "<init>":()V
 *   #29 = Utf8               java/lang/Object
 *   #30 = NameAndType        #15:#16        // object:Ljava/lang/Object;
 *   #31 = Class              #43            // java/lang/System
 *   #32 = NameAndType        #44:#45        // out:Ljava/io/PrintStream;
 *   #33 = Utf8               hello world
 *   #34 = Class              #46            // java/io/PrintStream
 *   #35 = NameAndType        #47:#48        // println:(Ljava/lang/String;)V
 *   #36 = Class              #49            // java/lang/Math
 *   #37 = NameAndType        #50:#51        // random:()D
 *   #38 = Utf8               java/lang/RuntimeException
 *   #39 = Utf8               end
 *   #40 = Utf8               welcome
 *   #41 = Utf8               com/openmind/monitor/ObjectLock
 *   #42 = Utf8               java/lang/Throwable
 *   #43 = Utf8               java/lang/System
 *   #44 = Utf8               out
 *   #45 = Utf8               Ljava/io/PrintStream;
 *   #46 = Utf8               java/io/PrintStream
 *   #47 = Utf8               println
 *   #48 = Utf8               (Ljava/lang/String;)V
 *   #49 = Utf8               java/lang/Math
 *   #50 = Utf8               random
 *   #51 = Utf8               ()D
 * {
 *   public com.openmind.monitor.ObjectLock();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC
 *     Code:
 *       stack=3, locals=1, args_size=1
 *          0: aload_0
 *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *          4: aload_0
 *          5: new           #2                  // class java/lang/Object
 *          8: dup
 *          9: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *         12: putfield      #3                  // Field object:Ljava/lang/Object;
 *         15: return
 *       LineNumberTable:
 *         line 151: 0
 *         line 152: 4
 *
 *   public void method1();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC
 *     Code:
 *       stack=4, locals=3, args_size=1
 *          0: aload_0
 *          1: getfield      #3                  // Field object:Ljava/lang/Object;
 *          4: dup
 *          5: astore_1
 *          6: monitorenter
 *          7: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *         10: ldc           #5                  // String hello world
 *         12: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *         15: invokestatic  #7                  // Method java/lang/Math.random:()D
 *         18: ldc2_w        #8                  // double 2.0d
 *         21: drem
 *         22: dconst_0
 *         23: dcmpl
 *         24: ifne          35
 *         27: new           #10                 // class java/lang/RuntimeException
 *         30: dup
 *         31: invokespecial #11                 // Method java/lang/RuntimeException."<init>":()V
 *         34: athrow
 *         35: aload_1
 *         36: monitorexit
 *         37: goto          45
 *         40: astore_2
 *         41: aload_1
 *         42: monitorexit
 *         43: aload_2
 *         44: athrow
 *         45: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *         48: ldc           #12                 // String end
 *         50: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *         53: return
 *       Exception table:
 *          from    to  target type
 *              7    37    40   any
 *             40    43    40   any
 *       LineNumberTable:
 *         line 155: 0
 *         line 156: 7
 *         line 157: 15
 *         line 158: 27
 *         line 160: 35
 *         line 161: 45
 *         line 162: 53
 *       StackMapTable: number_of_entries = 3
 *         frame_type = 252 // append
 *           offset_delta = 35
 *           locals = [ class java/lang/Object ]
 *         frame_type = 68 // same_locals_1_stack_item
 *           stack = [ class java/lang/Throwable ]
 *         frame_type = 250 // chop
 *           offset_delta = 4
 *
 *   public synchronized void method2();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC, ACC_SYNCHRONIZED
 *     Code:
 *       stack=2, locals=1, args_size=1
 *          0: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *          3: ldc           #13                 // String welcome
 *          5: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *          8: return
 *       LineNumberTable:
 *         line 165: 0
 *         line 166: 8
 * }
 * SourceFile: "ObjectLock.java"
 * </code>
 * @author zhoujunwen
 * @date 2020-10-23 13:44
 * @desc
 */
public class ObjectLock {
    private Object object = new Object();

    public void method1() {
        synchronized (object) {
            System.out.println("hello world");
            if (Math.random() % 2 == 0) {
                throw new RuntimeException();
            }
        }
        System.out.println("end");
    }

    public synchronized void method2() {
        System.out.println("welcome");
    }
}
