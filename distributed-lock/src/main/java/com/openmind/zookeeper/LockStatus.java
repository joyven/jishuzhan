package com.openmind.zookeeper;

/**
 * 锁状态
 *
 * @author zhoujunwen
 * @date 2019-10-09
 * @time 11:47
 * @desc
 */
public enum LockStatus {
    TRY_LOCK,
    LOCKED,
    UNLOCK
}
