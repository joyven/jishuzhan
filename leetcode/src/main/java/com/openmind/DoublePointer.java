package com.openmind;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * DoublePointer
 *
 * @author zhoujunwen
 * @date 2020-09-11
 * @time 14:53
 * @desc
 */
public class DoublePointer {
    private final static HashSet<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));

    public static void main(String[] args) {
        int[] numbers = {3, 2, 4};
        int target = 6;
        int[] res = towSum(numbers, target);
        if (res != null) {
            System.out.println(String.format("index1 = %d, index2 = %d", res[0], res[1]));
        }

        res = twoSumNotOrder(numbers, target);
        if (res != null) {
            System.out.println(String.format("index1 = %d, index2 = %d", res[0], res[1]));
        }

        System.out.println(judgeSquareSum(100));

        System.out.println(reverseVowels("abehdoeeruAisdOERU39023jubjkweow"));

        System.out.println(validPalindrome("abcbedbcfa"));
    }

    /**
     * 在有序数组中找出两个数，使它们的和为 target。
     * <p>
     * 使用双指针，一个指针指向值较小的元素，一个指针指向值较大的元素。指向较小元素的指针从头向尾遍历，指向较大元素的指针从尾向头遍历。
     * <p>
     * 如果两个指针指向元素的和 sum == target，那么得到要求的结果；
     * 如果 sum > target，移动较大的元素，使 sum 变小一些；
     * 如果 sum < target，移动较小的元素，使 sum 变大一些。
     * 数组中的元素最多遍历一次，时间复杂度为 O(N)。只使用了两个额外变量，空间复杂度为 O(1)。
     *
     * @param numbers
     * @param target
     * @return
     */
    public static int[] towSum(int[] numbers, int target) {
        if (numbers == null) return null;
        int i = 0, j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];
            if (sum == target) {
                return new int[]{i, j};
            }
            if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
        return null;
    }

    public static int[] twoSumNotOrder(int[] nums, int target) {
        if (nums == null) {
            return null;
        }
        Map<Integer, Integer> numbers = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            numbers.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int findValue = target - nums[i];
            if (numbers.containsKey(findValue) && numbers.get(findValue) != i) {
                return new int[]{numbers.get(findValue), i};
            }
        }
        return null;
    }


    /**
     * 题目描述：判断一个非负整数是否为两个整数的平方和。
     * <p>
     * 可以看成是在元素为 0~target 的有序数组中查找两个数，使得这两个数的平方和为 target，如果能找到，则返回 true，表示 target 是两个整数的平方和。
     * <p>
     * 本题和 167. Two Sum II - Input array is sorted 类似，只有一个明显区别：一个是和为 target，一个是平方和为 target。本题同样可以使用双指针得到两个数，使其平方和为 target。
     * <p>
     * 本题的关键是右指针的初始化，实现剪枝，从而降低时间复杂度。设右指针为 x，左指针固定为 0，为了使 02 + x2 的值尽可能接近 target，我们可以将 x 取为 sqrt(target)。
     * <p>
     * 因为最多只需要遍历一次 0~sqrt(target)，所以时间复杂度为 O(sqrt(target))。又因为只使用了两个额外的变量，因此空间复杂度为 O(1)。
     *
     * @param target
     * @return
     */
    public static boolean judgeSquareSum(int target) {
        if (target < 0) return false;
        int i = 0, j = (int) Math.sqrt(target);
        while (i <= j) {
            int powSum = i * i + j * j;
            if (powSum == target) {
                System.out.println(i + "," + j);
                return true;
            }

            if (powSum > target) {
                j--;
            } else {
                i++;
            }
        }
        return false;
    }

    /**
     * 使用双指针，一个指针从头向尾遍历，一个指针从尾到头遍历，当两个指针都遍历到元音字符时，交换这两个元音字符。
     * <p>
     * 为了快速判断一个字符是不是元音字符，我们将全部元音字符添加到集合 HashSet 中，从而以 O(1) 的时间复杂度进行该操作。
     * <p>
     * 时间复杂度为 O(N)：只需要遍历所有元素一次
     * 空间复杂度 O(1)：只需要使用两个额外变量
     *
     * @param s
     * @return
     */
    public static String reverseVowels(String s) {
        if (s == null) return null;
        int i = 0, j = s.length() - 1;
        char[] result = new char[s.length()];
        while (i <= j) {
            char ci = s.charAt(i);
            char cj = s.charAt(j);
            if (!vowels.contains(ci)) {
                result[i++] = ci;
            } else if (!vowels.contains(cj)) {
                result[j--] = cj;
            } else {
                result[i++] = cj;
                result[j--] = ci;
            }
        }
        return new String(result);
    }


    /**
     * 题目描述：可以删除一个字符，判断是否能构成回文字符串。
     * <p>
     * 所谓的回文字符串，是指具有左右对称特点的字符串，例如 "abcba" 就是一个回文字符串。
     * <p>
     * 使用双指针可以很容易判断一个字符串是否是回文字符串：令一个指针从左到右遍历，一个指针从右到左遍历，
     * 这两个指针同时移动一个位置，每次都判断两个指针指向的字符是否相同，如果都相同，字符串才是具有左右对称性质的回文字符串。
     * <p>
     * 本题的关键是处理删除一个字符。在使用双指针遍历字符串时，如果出现两个指针指向的字符不相等的情况，我们就试着删除一个字符，再判断删除完之后的字符串是否是回文字符串。
     * <p>
     * 在判断是否为回文字符串时，我们不需要判断整个字符串，因为左指针左边和右指针右边的字符之前已经判断过具有对称性质，所以只需要判断中间的子字符串即可。
     * <p>
     * 在试着删除字符时，我们既可以删除左指针指向的字符，也可以删除右指针指向的字符。
     *
     * @param s
     * @return
     */
    public static boolean validPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return isPalindrome(s, i, j - 1) || isPalindrome(s, i + 1, j);
            }
        }
        return true;
    }

    private static boolean isPalindrome(String s, int i, int j) {
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 题目描述：把归并结果存到第一个数组上。
     * <p>
     * 需要从尾开始遍历，否则在 nums1 上归并得到的值会覆盖还未进行归并比较的值
     * 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
     * <p>
     * 说明:
     * <p>
     * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
     * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/merge-sorted-array
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int index1 = m - 1, index2 = n - 1;
        int indexMerge = m + n - 1;
        while (index1 >= 0 || index2 >= 0) {
            if (index1 < 0) {
                nums1[indexMerge--] = nums2[index2--];
            } else if (index2 < 0) {
                nums1[indexMerge--] = nums1[index1--];
            } else if (nums1[index1] > nums2[index2]) {
                nums1[indexMerge--] = nums1[index1--];
            } else {
                nums1[indexMerge--] = nums2[index2--];
            }
        }
    }

    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums1, 0, nums2, m, n);
        Arrays.sort(nums1);
    }


}
