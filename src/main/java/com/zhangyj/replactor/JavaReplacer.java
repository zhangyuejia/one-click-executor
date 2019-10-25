package com.zhangyj.replactor;

import com.emp.bean.Config;

/**
 * java文件路径替换器
 * @author ZHANG
 */
public class JavaReplacer implements Replacer {

    @Override
    public String replace(String data) {
        return Config.getInstance().getTargetFilePrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\").replace(".java", ".class");
    }
}
