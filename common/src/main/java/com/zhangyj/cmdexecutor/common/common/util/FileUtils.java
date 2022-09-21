package com.zhangyj.cmdexecutor.common.common.util;

import java.net.URL;

/**
 * @author zhangyj
 */
public class FileUtils {

    public static String getResourcePath(String filePath) {
        URL url = FileUtils.class.getClassLoader().getResource(filePath);
        if(url == null){
            throw new IllegalArgumentException(filePath + "文件不存在");
        }
        return url.getPath().substring(1);
    }
}
