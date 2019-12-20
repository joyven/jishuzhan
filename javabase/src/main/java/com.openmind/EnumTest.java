package com.openmind;

/**
 * jishuzhan
 *
 * @author zhoujunwen
 * @date 2019-12-20
 * @time 11:26
 * @desc
 */
enum ColorEnum {
    RED,
    BLUE,
    YELLOW,
    GREEN
}

enum ColorsEnum {
    RED("红色", 1),
    BLUE("蓝色", 2),
    YELLOW("黄色", 3),
    GREEN("绿色", 4);
    ColorsEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
    private String name;
    private int index;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}

class EnumTest {
    public static void main(String[] args) {
        ColorEnum color = ColorEnum.GREEN;
        switch (color) {
            case RED:
                System.out.println("Red");
                break;
            case BLUE:
                System.out.println("Blue");
                break;
            case YELLOW:
                System.out.println("Yellow");
                break;
            case GREEN:
                System.out.println("Green");
                break;
            default:
                break;
        }

        System.out.println(ColorsEnum.RED.getName());
        System.out.println(ColorsEnum.RED.getIndex());
        System.out.println(ColorsEnum.values()[0].name());
    }
}
