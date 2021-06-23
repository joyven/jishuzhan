package com.openmind;

import java.util.Stack;

/**
 * Quene
 *
 * @author zhoujunwen
 * @date 2021-06-22 15:09
 * @desc
 */
public class Quene {
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;

    public Quene() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() throws Exception {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            throw new Exception("队列为空" );
        }
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }
}
