package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author super lollipop
 * @date 20-2-24
 */
public class TimeUtils {
    public static String getNowTimeFormatString(){
        SimpleDateFormat sf_ = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf_.format(new Date());
    }

    public static String formatDate(Date date,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
