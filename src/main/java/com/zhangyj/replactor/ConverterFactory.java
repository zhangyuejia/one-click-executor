package com.zhangyj.replactor;

import com.zhangyj.constant.Constant;
import com.zhangyj.replactor.impl.JavaCopyListConverter;
import com.zhangyj.replactor.impl.ResourceCopyListConverter;
import com.zhangyj.replactor.impl.WebRootCopyListConverter;
import org.springframework.stereotype.Component;

/**
 * 路径替换器工厂
 * @author ZHANG
 */
@Component
public class ConverterFactory {

    private final WebRootCopyListConverter webRootReplacer;

    private final JavaCopyListConverter javaReplacer;

    private final ResourceCopyListConverter resourceReplacer;

    public ConverterFactory(WebRootCopyListConverter webRootReplacer, JavaCopyListConverter javaReplacer, ResourceCopyListConverter resourceReplacer) {
        this.webRootReplacer = webRootReplacer;
        this.javaReplacer = javaReplacer;
        this.resourceReplacer = resourceReplacer;
    }

    /**
     * 获取路径替换器
     * @param relativePath 文件路径
     * @return 文件路径替换器
     */
    public BaseCopyListConverter getConverter(String relativePath){
        if(relativePath.startsWith(Constant.REPLACTOR_WEB_ROOT_PREFIX)){
            return webRootReplacer;
        }else if(relativePath.endsWith(Constant.REPLACTOR_JAVA_SUFFIX)) {
            return javaReplacer;
        }else if(relativePath.startsWith(Constant.REPLACTOR_PRESOURCE_REFIX)){
            return resourceReplacer;
        }
        return null;
    }
}
