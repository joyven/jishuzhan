package com.openmind;

/**
 * DoubleArraySearch
 *
 * @author zhoujunwen
 * @date 2021-06-22 10:36
 * @desc
 */
public class DoubleArraySearch {
    public static void main(String[] args) {
        int[][] arrays = new int[][]{
                {12, 34, 56, 78},
                {18, 35, 59, 90},
                {20, 40, 61, 100},
                {21, 66, 79, 102}};
        int target = 40;
        System.out.println(find(arrays, target));
    }

    /**
     * 右上角检索方法
     *
     * @param arr    数组
     * @param target 目标数字
     * @return
     */
    public static boolean find(int[][] arr, int target) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        int row = 0;
        int col = arr[row].length - 1;
        while (row < arr.length && col >= 0) {
            if (arr[row][col] == target) {
                return true;
            }
            if (arr[row][col] < target) {
                row++;
            } else {
                col--;
            }
        }
        return false;
    }

    public boolean binaryFind(int[][] arr, int target) {
        return false;
    }
}
