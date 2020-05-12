package com.util;

/**
 * @author super lollipop
 * @date 20-2-24
 * 裁剪字符串的工具类
 */

public class CutUtils {
    /**剪掉string ‘/’ 后面的字符
     * @param string 字符串
     * @param count ‘/’数*/
    public static String cut(String string,int count){
        StringBuffer stringBuffer = new StringBuffer(string);
        for (int i=0;i<count;i++) {
            string = stringBuffer.substring(0,string.lastIndexOf("/"));
            stringBuffer = new StringBuffer(string);
        }
        return stringBuffer.toString();
    }

}
