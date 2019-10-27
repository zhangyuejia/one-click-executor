package com.zhangyj.pojo;

/** 路径基类
 * @author zhangyj
 */
public class FilePath {

    protected String path;

    public FilePath(String path) {
        this.path = path;
    }

    public String getDir() {
        return path.substring(0, path.lastIndexOf("/" ));
    }

    public String getFileName(){
        return path.substring(getDir().length() + 1, path.lastIndexOf("."));
    }
}
