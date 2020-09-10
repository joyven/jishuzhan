package com.openmind;

/**
 * FieldVisibilityTest
 * https://juejin.im/post/6870149468038103053?utm_source=gold_browser_extension
 *
 * @author zhoujunwen
 * @date 2020-09-09
 * @time 14:18
 * @desc
 */
public class FieldVisibilityTest {
    int a = 1;
    int b = 2;

    private void change() {
        a = 3;
        b = a;
    }

    private void print() {
        System.out.println("a = " + a + ",b = " + b);
    }

    /**
     * 输出结果：
     * 1. a = 3,b = 3 // A线程先执行完
     * 2. a = 1,b = 2 // B线程先执行完
     * 3. a = 3,b = 2 // A和B线程交替执行
     * 4. a = 1,b = 3 // 由于内存可见性问题，导致a被修改后没有及时刷新回主内存就被打印了
     *
     * @param args
     */
    public static void main(String[] args) {
        while (true) {
            FieldVisibilityTest test = new FieldVisibilityTest();
            // 该线程负责改变a和b的值
            new Thread(() -> {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.change();
            }).start();

            // 该线程负责打印a和b的值
            new Thread(() -> {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.print();
            }).start();
        }
    }

}
