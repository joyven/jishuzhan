package com.openmind;

/**
 * jishuzhan
 *
 * 细探 Java class Format，分析文章：
 * 2 类文件结构
 *   2.1 Class类文件结构
 *   2.2 魔数
 *   2.3 Class文件的版本
 *   2.4 常量池
 *     2.4.1 CONSTANT_Utf8_info
 *     2.4.2 CONSTANT_Integer_info
 *     2.4.3 CONSTANT_Float_info
 *     2.4.4 CONSTANT_Long_info
 *     2.4.5 CONSTANT_Double_info
 *     2.4.6 CONSTANT_Class_info
 *     2.4.7 CONSTANT_String_info
 *     2.4.8 CONSTANT_Fieldref_info
 *     2.4.9 CONSTANT_Methodref_info
 *     2.4.10 CONSTANT_InterfaceMethodref_info
 *     2.4.11 CONSTANT_NameAndType_info
 *     2.4.12 CONSTANT_MethodHandle_info
 *     2.4.13 CONSTANT_MethodType_info
 *     2.4.14 CONSTANT_InvokeDynamic_info
 *   2.5 访问标志
 *   2.6 类、父类与接口索引的集合
 *   2.7 字段表集合
 *   2.8 方法集合
 *   2.9 属性表集
 *     2.9.1 ConstantValue
 *     2.9.2 Code
 *     2.9.3 StackMapTable
 *     2.9.4 Exceptions
 *     2.9.5 BootstrapMethods
 *     2.9.6 InnerClasses
 *     2.9.7 EnclosingMethod
 *     2.9.8 Synthetic
 *     2.9.9 Signature
 *     2.9.10 RuntimeVisibleAnnotations
 *     2.9.11 RuntimeInvisibleAnnotations
 *     2.9.12 RuntimeVisibleParameterAnnotations
 *     2.9.13 RuntimeInvisibleParameterAnnotations
 *     2.9.14 RuntimeVisibleTypeAnnotations
 *     2.9.15 RuntimeInvisibleTypeAnnotations
 *     2.9.16 AnnotationDefault
 *     2.9.17 MethodParameters
 *     2.9.18 SourceFile
 *     2.9.19 SourceDebugExtension
 *     2.9.20 LineNumberTable
 *     2.9.21 LocalVariableTable
 *     2.9.22 LocalVariableTypeTable
 *     2.9.23 Deprecated
 * 3 JVM的限制
 *
 * https://www.zhoujunwen.com/2019/%e6%b7%b1%e5%85%a5%e7%90%86%e8%a7%a3%e7%b1%bb%e6%96%87%e4%bb%b6%e7%bb%93%e6%9e%84
 *
 * @author zhoujunwen
 * @date 2019-11-17
 * @time 20:28
 * @desc
 */
public class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
