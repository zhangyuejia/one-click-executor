package com.zhangyj.cmdexecutor.core.common.util;

import java.io.File;
import java.net.URL;

/**
 * @author zhangyj
 */
public class FileUtils {

    public static final String CLASSPATH = "classpath:";

    public static String getResourcePath(String resource) {
        if(new File(resource).exists()){
            return resource;
        }
        if(resource.startsWith(CLASSPATH)){
            resource = resource.substring(CLASSPATH.length());
        }
        URL url = FileUtils.class.getClassLoader().getResource("");
        if(url == null){
            throw new IllegalArgumentException("路径不存在");
        }
        return url.getPath().substring(1) + resource;
    }

    public static String getAbsolutePath(String resource) throws Exception {
        return new File(resource).getCanonicalPath();
    }


    public static String getResourcePath() {
        return getResourcePath("");
    }
}
