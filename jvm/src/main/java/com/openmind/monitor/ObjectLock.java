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
 *
 * 得到结果：
 *Classfile /Users/zhoujunwen/Documents/workspace/jishuzhan/jvm/target/classes/com/openmind/monitor/ObjectLock.class
 *  Last modified 2020-10-23; size 822 bytes
 *  MD5 checksum 0f8e0c24ae5ca53063818e4159a0c085
 *  Compiled from "ObjectLock.java"
 *public class com.openmind.monitor.ObjectLock
 *  minor version: 0
 *  major version: 52
 *  flags: ACC_PUBLIC, ACC_SUPER
 *Constant pool:
 *   #1 = Methodref          #2.#28         // java/lang/Object."<init>":()V
 *   #2 = Class              #29            // java/lang/Object
 *   #3 = Fieldref           #10.#30        // com/openmind/monitor/ObjectLock.object:Ljava/lang/Object;
 *   #4 = Fieldref           #31.#32        // java/lang/System.out:Ljava/io/PrintStream;
 *   #5 = String             #33            // hello world
 *   #6 = Methodref          #34.#35        // java/io/PrintStream.println:(Ljava/lang/String;)V
 *   #7 = Class              #36            // java/lang/RuntimeException
 *   #8 = Methodref          #7.#28         // java/lang/RuntimeException."<init>":()V
 *   #9 = String             #37            // welcome
 *  #10 = Class              #38            // com/openmind/monitor/ObjectLock
 *  #11 = Utf8               object
 *  #12 = Utf8               Ljava/lang/Object;
 *  #13 = Utf8               <init>
 *  #14 = Utf8               ()V
 *  #15 = Utf8               Code
 *  #16 = Utf8               LineNumberTable
 *  #17 = Utf8               LocalVariableTable
 *  #18 = Utf8               this
 *  #19 = Utf8               Lcom/openmind/monitor/ObjectLock;
 *  #20 = Utf8               method1
 *  #21 = Utf8               StackMapTable
 *  #22 = Class              #38            // com/openmind/monitor/ObjectLock
 *  #23 = Class              #29            // java/lang/Object
 *  #24 = Class              #39            // java/lang/Throwable
 *  #25 = Utf8               method2
 *  #26 = Utf8               SourceFile
 *  #27 = Utf8               ObjectLock.java
 *  #28 = NameAndType        #13:#14        // "<init>":()V
 *  #29 = Utf8               java/lang/Object
 *  #30 = NameAndType        #11:#12        // object:Ljava/lang/Object;
 *  #31 = Class              #40            // java/lang/System
 *  #32 = NameAndType        #41:#42        // out:Ljava/io/PrintStream;
 *  #33 = Utf8               hello world
 *  #34 = Class              #43            // java/io/PrintStream
 *  #35 = NameAndType        #44:#45        // println:(Ljava/lang/String;)V
 *  #36 = Utf8               java/lang/RuntimeException
 *  #37 = Utf8               welcome
 *  #38 = Utf8               com/openmind/monitor/ObjectLock
 *  #39 = Utf8               java/lang/Throwable
 *  #40 = Utf8               java/lang/System
 *  #41 = Utf8               out
 *  #42 = Utf8               Ljava/io/PrintStream;
 *  #43 = Utf8               java/io/PrintStream
 *  #44 = Utf8               println
 *  #45 = Utf8               (Ljava/lang/String;)V
 *{
 *  public com.openmind.monitor.ObjectLock();
 *    descriptor: ()V
 *    flags: ACC_PUBLIC
 *    Code:
 *      stack=3, locals=1, args_size=1
 *         0: aload_0
 *         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *         4: aload_0
 *         5: new           #2                  // class java/lang/Object
 *         8: dup
 *         9: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *        12: putfield      #3                  // Field object:Ljava/lang/Object;
 *        15: return
 *      LineNumberTable:
 *        line 10: 0
 *        line 11: 4
 *      LocalVariableTable:
 *       Start  Length  Slot  Name   Signature
 *            0      16     0  this   Lcom/openmind/monitor/ObjectLock;
 *
 *  public void method1();
 *    descriptor: ()V
 *    flags: ACC_PUBLIC
 *    Code:
 *      stack=2, locals=3, args_size=1
 *         0: aload_0
 *         1: getfield      #3                  // Field object:Ljava/lang/Object;
 *         4: dup
 *         5: astore_1
 *         6: monitorenter
 *         7: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *        10: ldc           #5                  // String hello world
 *        12: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *        15: new           #7                  // class java/lang/RuntimeException
 *        18: dup
 *        19: invokespecial #8                  // Method java/lang/RuntimeException."<init>":()V
 *        22: athrow
 *        23: astore_2
 *        24: aload_1
 *        25: monitorexit
 *        26: aload_2
 *        27: athrow
 *      Exception table:
 *         from    to  target type
 *             7    26    23   any
 *      LineNumberTable:
 *        line 14: 0
 *        line 15: 7
 *        line 16: 15
 *        line 17: 23
 *      LocalVariableTable:
 *        Start  Length  Slot  Name   Signature
 *            0      28     0  this   Lcom/openmind/monitor/ObjectLock;
 *      StackMapTable: number_of_entries = 1
 *        frame_type = 255
 *          offset_delta = 23
 *         locals = [ class com/openmind/monitor/ObjectLock, class java/lang/Object ]
 *          stack = [ class java/lang/Throwable ]
 *
 *  public synchronized void method2();
 *    descriptor: ()V
 *    flags: ACC_PUBLIC, ACC_SYNCHRONIZED
 *    Code:
 *      stack=2, locals=1, args_size=1
 *         0: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *         3: ldc           #9                  // String welcome
 *         5: invokevirtual #6                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *         8: return
 *      LineNumberTable:
 *        line 21: 0
 *        line 22: 8
 *      LocalVariableTable:
 *        Start  Length  Slot  Name   Signature
 *            0       9     0  this   Lcom/openmind/monitor/ObjectLock;
 *}
 SourceFile: "ObjectLock.java"
 *
 * @author zhoujunwen
 * @date 2020-10-23 13:44
 * @desc
 */
public class ObjectLock {
    private Object object = new Object();

    public void method1() {
        synchronized (object) {
            System.out.println("hello world");
            throw new RuntimeException();
        }
    }

    public synchronized void method2() {
        System.out.println("welcome");
    }
}
