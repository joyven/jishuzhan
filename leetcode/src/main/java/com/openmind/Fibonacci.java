package com.openmind;

import java.util.ArrayList;
import java.util.List;

/**
 * Fibonacci
 *
 * @author zhoujunwen
 * @date 2021-06-22 16:49
 * @desc
 */
public class Fibonacci {

    public static void main(String[] args) {
        Fibonacci f = new Fibonacci();
        List<Long> rs = f.fabonacci(10);
        System.out.println(rs.get(rs.size()-1));


        long rs2 = f.fabonacci2(10);
        System.out.println(rs2);
    }

    public List<Long> fabonacci(int n) {
        List<Long> list = new ArrayList<>(n > 0 ? n : 1);
        if (n < 0) {
            return list;
        }

        long preOne = 1L;
        long preTwo = 0L;

        list.add(preOne);
        for (int i = 2; i <= n; i++) {
            long rs = preOne + preTwo;
            preTwo = preOne;
            preOne = rs;
            list.add(rs);
        }
        return list;
    }

    public long fabonacci2(int n) {
        if (n < 0 || n == 0) {
            return 0L;
        }
        if ( n == 1) {
            return 1L;
        }
         return fabonacci2(n-1) + fabonacci2(n -2);
    }
}
