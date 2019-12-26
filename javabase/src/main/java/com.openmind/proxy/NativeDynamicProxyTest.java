package com.openmind.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jishuzhan
 *
 * JDK Proxy 只能代理实现接口的类（即使是 extends 继承类也是不可以代理的）。
 *
 * @author zhoujunwen
 * @date 2019-12-25
 * @time 16:14
 * @desc
 */
interface Animal {
    void eat();
}

class Dog implements Animal {

    @Override
    public void eat() {
        System.out.println("The dog is eating!");
    }
}

class Cat implements Animal {

    @Override
    public void eat() {
        System.out.println("The cat is eating!");
    }
}

/**
 * JDK原生动态代理
 */
class AnimalProxy implements InvocationHandler {

    /**
     * 代理对象
     */
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用前");
        System.out.println(target);
        Object result = method.invoke(target, args);
        System.out.println("调用后");
        return result;
    }
}

/**
 * @author zhoujunwen
 */
public class NativeDynamicProxyTest {

    public static void main(String[] args) {
        AnimalProxy proxy = new AnimalProxy();
        Animal dogProxy = (Animal) proxy.getInstance(new Dog());
        dogProxy.eat();


        Animal catProxy = (Animal) Proxy.newProxyInstance(Animal.class.getClassLoader(), new Class[]{Animal.class},
                (proxy1, method, args1) -> {
            System.out.println("调用前");
            Object result = method.invoke(Cat.class.newInstance(), args1);
            System.out.println("调用后");
            return result;
        });
        catProxy.eat();
    }
}

