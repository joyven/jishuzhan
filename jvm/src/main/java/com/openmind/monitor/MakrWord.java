package com.openmind.monitor;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * MakrWord
 *
 * @author zhoujunwen
 * @date 2020-11-30 15:26
 * @desc
 */
public class MakrWord {
    public static void main(String[] args) {
        // 打印 JVM 的详细信息
        System.out.println(VM.current().details());
        // 打印对应的对象头信息
        System.out.println(ClassLayout.parseInstance(new A()).toPrintable());
    }
}

class A{
}
