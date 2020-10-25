package com.openmind.decoratorproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * jishuzhan
 * <p>
 * cglib底层是通过子类集成被代理对象的方式实现动态代理的，因此代理类不能是最终类(final)
 * 否则会报错误：ava.lang.IllegalArgumentException: Cannot subclass final class xxx
 *
 * @author zhoujunwen
 * @date 2019-12-26
 * @time 09:46
 * @desc
 */

class Panda {
    public void eat() {
        System.out.println("The panda is eating!");
    }
}

class CglibProxy implements MethodInterceptor {
    /**
     * 代理对象
     */
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        // 设置父类为实例类
        enhancer.setSuperclass(this.target.getClass());
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用前");
        // 执行方法调用
        Object result = methodProxy.invokeSuper(o, objects);
        Object res = method.invoke(target, objects);
        Object r = methodProxy.invoke(target, objects);
        System.out.println("调用后");
        return result;
    }
}

public class CglibProxyTest {
    public static void main(String[] args) {
        // cglib代理调用
        CglibProxy proxy = new CglibProxy();
        Panda panda = (Panda) proxy.getInstance(new Panda());
        panda.eat();
    }
}
