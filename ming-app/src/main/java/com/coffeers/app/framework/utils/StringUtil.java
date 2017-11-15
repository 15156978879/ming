package com.coffeers.app.framework.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public class StringUtil {
    /*
    * 判断字符串是否为空(为空的标准是 str==null 或 str.length()==0,返回true )
    * StringUtils.isEmpty(null) = true
    * StringUtils.isEmpty("") = true
    * StringUtils.isEmpty(" ") = false //注意在 StringUtils 中空格作非空处理
    * StringUtils.isEmpty("   ") = false
    * StringUtils.isEmpty("bob") = false
    * StringUtils.isEmpty(" bob ") = false
    * */
    public static boolean isEmpty(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }
    /*
    * 判断字符串是否非空
    * */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /*
    * 判断某字符串是否为空或长度为0或由空白符(whitespace) 构成
    * StringUtils.isBlank(null) = true
    * StringUtils.isBlank("") = true
    * StringUtils.isBlank(" ") = true
    * StringUtils.isBlank("        ") = true
    * StringUtils.isBlank("/t /n /f /r") = true   //对于制表符、换行符、换页符和回车符
    * StringUtils.isBlank()   //均识为空白符
    * StringUtils.isBlank("/b") = false   //"/b"为单词边界符
    * StringUtils.isBlank("bob") = false
    * StringUtils.isBlank(" bob ") = false
    * */
    public static boolean isBlank(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isBlank(str);
    }
    /*
     * 判断某字符串是否不为空且长度不为0且不由空白符(whitespace) 构成，等于 !isBlank(String str)
     * */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }


    /**
     * 分割字符串
     * @param str
     * @return
     */
    public static String[] splitString(String str,String s) {
        if (isNotEmpty(str)){
            return StringUtils.split(str,s);
        }
        return null;
    }

    /**
     * 判断是否只包含0-9
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isNumeric(str);
    }

    /**
     * 判断是否只包含字母
     * @param str
     * @return
     */
    public static boolean isAlpha(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isAlpha(str);
    }

    /**
     * 判断是否只包含字母和数字的组合
     * @param str
     * @return
     */
    public static boolean isAlphanumeric(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isAlphanumeric(str);
    }

    /**
     * 判断是否只包含空格和字母
     * @param str
     * @return
     */
    public static boolean isAlphaSpace(String str){
        if(str != null){
            str=str.trim();
        }
        return StringUtils.isAlphaSpace(str);
    }

}