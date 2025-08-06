package com.zhangyj.oneclick.core.common.util;

import java.time.Duration;

/**
 * @author zhang.yuejia1
 */
public class TimeUtils {

    public static String formatInterval(long interval){
        if(interval < 0){
            throw new IllegalArgumentException("time不应该小于0");
        }
        Duration duration = Duration.ofMillis(interval);
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long secs = duration.getSeconds();
        long millis = interval % 1000;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0) {
            sb.append(secs).append("秒");
        }
        if (millis > 0) {
            sb.append(millis).append("毫秒");
        }
        return sb.toString();
    }
}
