package com.openmind.zookeeper;

/**
 * 通过zk实现分布式锁
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 11:37
 * @desc
 */
public interface DistributedLock {
    /**
     * 加锁，如果加锁失败直接抛出异常
     * @throws Exception
     */
    void lock() throws Exception;

    /**
     * 尝试加锁，直到加锁成功，或者加锁失败抛出异常
     * @return
     * @throws Exception
     */
    boolean tryLock() throws Exception;

    /**
     * 尝试加锁，设置加锁超时时间，在millisecond内加锁成功，或者超时跑出异常
     * @param millisecond
     * @return
     * @throws Exception
     */
    boolean tryLock(long millisecond) throws Exception;

    /**
     * 释放锁
     * @throws Exception
     */
    void unlock() throws Exception;
}
