package com.emp.replactor;

import com.emp.bean.Constant;

/**
 * 路径替换器工厂
 * @author ZHANG
 */
public class PathReplactorFactory {

    /**
     * 获取路径替换器
     * @param data 文件路径
     * @return 文件路径替换器
     */
    public static PathReplactor getReplator(String data){
        PathReplactor replactor = null;
        if(data.startsWith(Constant.REPLACTOR_WEB_ROOT_PREFIX)){
            replactor = new WebRootPathReplactor();
        }else if(data.endsWith(Constant.REPLACTOR_JAVA_SUFFIX)) {
            replactor = new JavaPathReplactor();
        }else if(data.startsWith(Constant.REPLACTOR_PRESOURCE_REFIX)){
            replactor = new ResourcePathReplactor();
        }
        return replactor;
    }
}
