package com.openmind.zookeeper;

import com.openmind.zookeeper.curator.ZkExclusiveLock;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 14:48
 * @desc
 */
public class ZkExclusiveLockTest {
    public static void main(String[] args) throws Exception {
        String zkPath = "192.168.6.55:2181,192.168.6.56:2181,192.168.6.57:2181";
        String lockName= "test-zkexclusive-lock";
        DistributedLock distributedLock = new ZkExclusiveLock(zkPath, lockName);

//        distributedLock.lock();
        distributedLock.tryLock();
        distributedLock.tryLock(10000000);
    }
}
