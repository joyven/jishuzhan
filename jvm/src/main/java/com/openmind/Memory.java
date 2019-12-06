package com.openmind;

/**
 * jishuzhan
 *
 * totalMemory():方法用于获取当前虚拟机进程从操作系统申请的内存大小。
 * 虽然虚拟机可以向操作系统申请内存，但是申请大小是受到限制的，而且
 * 申请过程也是一个动态的过程，即使用多少，申请多少直到最大值，这样
 * 可以减少维护内存的开销。假如一次性申请最大容量的内存，且只用很小
 * 的一部分，那么维护这块内存将是一个不小的开销。</p>
 *
 *
 * freeMemory():方法用于获得当虚拟机进程从操作系统申请的内存空余量。
 * 为什么说是空余量呢，因为虽然虚拟机会动态申请内存，用多少申请多少，
 * 但是它仍然不能保证申请到的内存就一定够用，所以它每次申请的内存会
 * 有一点空余，那么通过这个方法就可以知道当前虚拟机有多大的空余内存。
 *
 * maxMemory():方法用于获取当前虚拟机进程从操作系统可申请内存的最大容量。
 *
 *
 * @author zhoujunwen
 * @date 2019-12-06
 * @time 11:52
 * @desc
 */
public class Memory {
    public static void main(String[] args) {
        //当前虚拟机进程从操作系统申请的内存大小
        System.out.println("当前内存:"+Runtime.getRuntime().totalMemory()/1024/1024+" MB");

        //当前虚拟机进程从操作系统申请的内存空余量
        System.out.println("空余内存:"+Runtime.getRuntime().freeMemory()/1024/1024+" MB");

        //当前虚拟机进程从操作系统可申请的最大内存容量
        System.out.println("可申请最大内存:"+ Runtime.getRuntime().maxMemory()/1024/1024+" MB");
    }
}
