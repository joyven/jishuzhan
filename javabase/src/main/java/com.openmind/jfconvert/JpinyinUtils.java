package com.openmind.jfconvert;

import com.github.stuxuhai.jpinyin.ChineseHelper;

/**
 * JpinyinUtils
 *
 * @author zhoujunwen
 * @date 2020-12-16 11:54
 * @desc
 */
public class JpinyinUtils {
    /**
     * 简体转换为繁体
     * @param pinYinStr 要转换的字符串
     * @return
     */
    public static String s2t(String pinYinStr) {
        String tempStr = null;
        try {
            tempStr = ChineseHelper.convertToTraditionalChinese(pinYinStr);
        } catch (Exception e) {
            tempStr = pinYinStr;
            e.printStackTrace();
        }
        return tempStr;
    }


    /**
     * 繁体转换为简体
     * @param pinYinSt 要转换的字符串
     * @return
     */
    public static String t2s(String pinYinSt) {
        String tempStr = null;
        try {
            tempStr = ChineseHelper.convertToSimplifiedChinese(pinYinSt);
        } catch (Exception e) {
            tempStr = pinYinSt;
            e.printStackTrace();
        }

        return tempStr;
    }

    public static void main(String[] args) {
        String s = "hankcs在台湾写代码";
        System.out.println(s2t(s));
    }
}
