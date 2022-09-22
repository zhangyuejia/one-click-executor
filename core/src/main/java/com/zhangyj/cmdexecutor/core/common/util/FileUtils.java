package com.zhangyj.cmdexecutor.core.common.util;

import java.io.File;
import java.net.URL;

/**
 * @author zhangyj
 */
public class FileUtils {

    public static String getResourcePath(String resource) {
        URL url = FileUtils.class.getClassLoader().getResource("");
        if(url == null){
            throw new IllegalArgumentException(resource + "文件不存在");
        }
        return url.getPath().substring(1) + resource;
    }

    public static boolean existsResource(String resource){
        return new File(getResourcePath(resource)).exists();
    }
}
