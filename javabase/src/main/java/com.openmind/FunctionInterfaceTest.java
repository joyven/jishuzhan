package com.openmind;

/**
 * jishuzhan
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 15:16
 * @desc
 */

/**
 * 函数式接口（Function Interface）是一个特殊的接口，使用 @FunctionInterface 注解声明，
 * 定义这种接口可以使用 Lambda 表达式直接调用。
 *
 */
@FunctionalInterface
interface IAnimal {
    String animalName= "Cat";

    /**
     * static 方法属于接口方法，可以直接使用
     */
    static void printName() {
        System.out.println(animalName);
    }

    /**
     *  default 属于实例方法，必须先创建实例
     */
    default void printAge() {
        System.out.println("12");
    }

    void sayHi(String name);
}

class FunctionInterfaceTest {
    public static void main(String[] args) {
        IAnimal.printName();
        /**
         * 使用 Lambda 表达式直接调用
         */
        IAnimal animal = System.out::println;
        animal.sayHi("Miao~Miao~");
        animal.printAge();
    }
}

//========================================================================

/**
 * 静态变量会被继承，静态方法不会被继承
 */
class AnimalImpl implements IAnimal {
    public static void main(String[] args) {
        /**
         * 静态变量会被继承
         */
        System.out.println(animalName);
        /**
         * 静态方法不会被继承
         */
        IAnimal.printName();
        IAnimal animal = new AnimalImpl();
        animal.sayHi("miao~~~~~miao~~~~");
    }

    @Override
    public void sayHi(String name) {
        System.out.println(name);
    }
}

