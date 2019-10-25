package com.zhangyj.replactor;

import com.zhangyj.constant.Constant;

/**
 * 路径替换器工厂
 * @author ZHANG
 */
public class ReplacerFactory {

    /**
     * 获取路径替换器
     * @param data 文件路径
     * @return 文件路径替换器
     */
    public static Replacer getReplacer(String data){
        Replacer replactor = null;
        if(data.startsWith(Constant.REPLACTOR_WEB_ROOT_PREFIX)){
            replactor = new WebRootReplacer();
        }else if(data.endsWith(Constant.REPLACTOR_JAVA_SUFFIX)) {
            replactor = new JavaReplacer();
        }else if(data.startsWith(Constant.REPLACTOR_PRESOURCE_REFIX)){
            replactor = new ResourceReplacer();
        }
        return replactor;
    }
}
