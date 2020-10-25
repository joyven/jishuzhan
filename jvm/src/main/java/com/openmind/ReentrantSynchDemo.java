package com.openmind;

/**
 * Last modified 2020-9-14; size 631 bytes
 * MD5 checksum 0d508989a6bb2f8901267fdab7b708b6
 * Compiled from "ReentrantSynchDemo.java"
 * public class com.openmind.ReentrantSynchDemo
 * minor version: 0
 * major version: 52
 * flags: ACC_PUBLIC, ACC_SUPER
 * Constant pool:
 * #1 = Methodref          #4.#24         // java/lang/Object."<init>":()V
 * #2 = Class              #25            // com/openmind/ReentrantSynchDemo
 * #3 = Methodref          #2.#26         // com/openmind/ReentrantSynchDemo.method:()V
 * #4 = Class              #27            // java/lang/Object
 * #5 = Utf8               <init>
 * #6 = Utf8               ()V
 * #7 = Utf8               Code
 * #8 = Utf8               LineNumberTable
 * #9 = Utf8               LocalVariableTable
 * #10 = Utf8               this
 * #11 = Utf8               Lcom/openmind/ReentrantSynchDemo;
 * #12 = Utf8               main
 * #13 = Utf8               ([Ljava/lang/String;)V
 * #14 = Utf8               args
 * #15 = Utf8               [Ljava/lang/String;
 * #16 = Utf8               StackMapTable
 * #17 = Class              #15            // "[Ljava/lang/String;"
 * #18 = Class              #27            // java/lang/Object
 * #19 = Class              #28            // java/lang/Throwable
 * #20 = Utf8               MethodParameters
 * #21 = Utf8               method
 * #22 = Utf8               SourceFile
 * #23 = Utf8               ReentrantSynchDemo.java
 * #24 = NameAndType        #5:#6          // "<init>":()V
 * #25 = Utf8               com/openmind/ReentrantSynchDemo
 * #26 = NameAndType        #21:#6         // method:()V
 * #27 = Utf8               java/lang/Object
 * #28 = Utf8               java/lang/Throwable
 * {
 * public com.openmind.ReentrantSynchDemo();
 * descriptor: ()V
 * flags: ACC_PUBLIC
 * Code:
 * stack=1, locals=1, args_size=1
 * 0: aload_0
 * 1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 * 4: return
 * LineNumberTable:
 * line 11: 0
 * LocalVariableTable:
 * Start  Length  Slot  Name   Signature
 * 0       5     0  this   Lcom/openmind/ReentrantSynchDemo;
 * <p>
 * * public static void main(java.lang.String[]);
 * descriptor: ([Ljava/lang/String;)V
 * flags: ACC_PUBLIC, ACC_STATIC
 * Code:
 * stack=2, locals=3, args_size=1
 * 0: ldc           #2                  // class com/openmind/ReentrantSynchDemo
 * 2: dup
 * 3: astore_1
 * 4: monitorenter          // main方法加锁指令
 * 5: aload_1
 * 6: monitorexit           // main 方法释放锁
 * 7: goto          15
 * 10: astore_2
 * 11: aload_1
 * 12: monitorexit
 * 13: aload_2
 * 14: athrow
 * 15: invokestatic  #3                  // Method method:()V
 * 18: return
 * Exception table:
 * from    to  target type
 * 5     7    10   any
 * 10    13    10   any
 * LineNumberTable:
 * line 13: 0
 * line 15: 5
 * line 16: 15
 * line 17: 18
 * LocalVariableTable:
 * Start  Length  Slot  Name   Signature
 * 0      19     0  args   [Ljava/lang/String;
 * StackMapTable: number_of_entries = 2
 * frame_type = 255  full_frame
 * offset_delta = 10
 * locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
 * stack = [ class java/lang/Throwable ]
 * frame_type = 250  chop
 * offset_delta = 4
 * MethodParameters:
 * Name                           Flags
 * args
 * <p>
 * public static synchronized void method();
 * descriptor: ()V
 * flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
 * Code:
 * stack=0, locals=0, args_size=0
 * 0: return
 * LineNumberTable:
 * line 21: 0
 * }
 * SourceFile: "ReentrantSynchDemo.java"
 *//** full_frame *//**
 *           offset_delta = 10
 *           locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
 *           stack = [ class java/lang/Throwable ]
 *         frame_type = 250 *//** chop *//**
 *           offset_delta = 4
 *     MethodParameters:
 *       Name                           Flags
 *       args
 *
 *  public static synchronized void method();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
 *     Code:
 *       stack=0, locals=0, args_size=0
 *          0: return
 *       LineNumberTable:
 *         line 21: 0
 * }
 * SourceFile: "ReentrantSynchDemo.java"
 */

/**
 * @author zhoujunwen
 * @date 2020-09-14
 * @time 10:10
 * @desc
 */
public class ReentrantSynchDemo {
    public static void main(String[] args) {
        synchronized (ReentrantSynchDemo.class) {
            method();
        }
    }

    public synchronized static void method() {

    }
}
