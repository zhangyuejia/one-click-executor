package com.zhangyj.tools.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 * @author zhangyj
 */
@Slf4j
public class ThreadUtils {

    public static void sleepQuiet(Long millSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(millSeconds);
        } catch (InterruptedException e) {
            log.error("线程睡眠异常", e);
        }
    }
}
