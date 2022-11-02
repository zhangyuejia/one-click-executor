package com.zhangyj.cmdexecutor.core.common.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeUtils {

    public static String formatInterval(long interval){
        if(interval < 0){
            throw new IllegalArgumentException("time不应该小于0");
        }
        if(interval >= 24 * 60 * 60 * 1000){
            return interval + "ms";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String[] split = formatter.format(interval).split(":");
        return getTimeDesc(split[0], "小时") + getTimeDesc(split[1], "分") + getTimeDesc(split[2], "秒");
    }

    private static String getTimeDesc(String time, String timeUnitStr){
        int timeInt = Integer.parseInt(time);
        return timeInt>0? timeInt+ timeUnitStr: "";
    }
}
