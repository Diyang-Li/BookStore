package com.atguigu.myssm.util;

/**
 * @author Diyang Li
 * @create 2022-04-04 11:23 AM
 */
public class StringUtil {
    public static boolean isEmpty(String str){
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
