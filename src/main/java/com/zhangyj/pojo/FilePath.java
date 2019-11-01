package com.zhangyj.pojo;

/** 路径基类
 * @author zhangyj
 */
class FilePath {

    private String path;

    FilePath(String path) {
        this.path = path;
    }

    String getDir() {
        return path.substring(0, path.lastIndexOf("/" ));
    }

    String getFileName(){
        return path.substring(getDir().length() + 1, path.lastIndexOf("."));
    }
}
