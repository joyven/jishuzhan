package com.openmind.zookeeper;

import com.openmind.zookeeper.curator.ZkReadWriteLock;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 14:47
 * @desc
 */
public class ZkReadLockTest {
    public static void main(String[] args) throws Exception {
        String zkPath = "192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181";
        ReadWriteLock readWriteLock = new ZkReadWriteLock(zkPath);

        readWriteLock.readLock().lock();
    }
}
