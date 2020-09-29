package com.openmind.decoratorproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * SecurityProxyInvocationHandler
 *
 * @author zhoujunwen
 * @date 2020-09-28 17:27
 * @desc
 */
public class SecurityProxyInvocationHandler implements InvocationHandler {
    private Object targetObject;

    public SecurityProxyInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (proxy instanceof Account && method.getName().equals("operator")) {
            System.out.println("检查账户安全");
        }
        return method.invoke(targetObject, args);
    }
}
