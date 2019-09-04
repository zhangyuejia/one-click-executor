package com.emp.replactor;

import com.emp.bean.Constant;

/**
 * @author ZHANG
 */
public class PathReplactorFactory {

    public static PathReplactor getInstance(String data){
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
