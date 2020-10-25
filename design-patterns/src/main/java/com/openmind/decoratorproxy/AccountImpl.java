package com.openmind.decoratorproxy;

/**
 * AccountImpl
 *
 * @author zhoujunwen
 * @date 2020-09-28 17:26
 * @desc
 */
public class AccountImpl implements Account {
    @Override
    public void operator() {
        System.out.println("这是账户操作方法");
    }
}
