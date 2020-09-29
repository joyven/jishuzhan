package com.openmind.decorator;

import com.openmind.decoratorproxy.*;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * ProxyTest
 *
 * @author zhoujunwen
 * @date 2020-09-28 17:30
 * @desc
 */
public class ProxyTest {
    @Test
    public void testProxy() {
        Account account = (Account) Proxy.newProxyInstance(Account.class.getClassLoader(),
                new Class[]{Account.class},
                new SecurityProxyInvocationHandler(new AccountImpl()));
        account.operator();
    }
}
