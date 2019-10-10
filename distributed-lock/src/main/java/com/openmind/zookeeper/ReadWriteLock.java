package com.openmind.zookeeper;

/**
 * 读写锁
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 15:15
 * @desc
 */
public interface ReadWriteLock {
    DistributedLock readLock();
    DistributedLock writeLock();
}
