package com.openmind.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * jishuzhan
 * 反射获取调用类可以通过 Class.forName()，反射获取类实例要通过 newInstance()，相当于 new 一个新对象，
 * 反射获取方法要通过 getMethod()，获取到类方法之后使用 invoke() 对类方法进行调用。
 * 果是类方法为私有方法的话，则需要通过 setAccessible(true) 来修改方法的访问限制，以上的这些操作就是反射的基本使用。
 *
 * @author zhoujunwen
 * @date 2019-12-25
 * @time 16:02
 * @desc
 */
public class ReflectTest {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        ReflectTest reflectTest = new ReflectTest();
        reflectTest.invokeStaticMd();
        reflectTest.invokePublicMd();
        reflectTest.invokePrivateMd();

    }


    /**
     * 反射调用静态方法
     *
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void invokeStaticMd() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Class clazz = Class.forName("com.openmind.reflect.MyReflect");
        Method method = clazz.getMethod("staticMethod");
        method.invoke(clazz);
    }

    /**
     * 反射调用公共方法
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private void invokePublicMd() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        Class clazz = Class.forName("com.openmind.reflect.MyReflect");
        // 创建实例，相当于 new
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("publicMethod");
        method.invoke(object);
    }

    private void invokePrivateMd() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        Class clazz = Class.forName("com.openmind.reflect.MyReflect");
        // 创建实例，相当于 new
        Object o = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("privateMethod");
        method.setAccessible(true);
        method.invoke(o);
    }

}
