package com.zhangyj.replactor;

import com.zhangyj.constant.Const;
import com.zhangyj.replactor.impl.JavaCopyListConverter;
import com.zhangyj.replactor.impl.ResourceCopyListConverter;
import com.zhangyj.replactor.impl.RmsCopyListConverter;
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

    private final RmsCopyListConverter rmsCopyListConverter;

    public ConverterFactory(WebRootCopyListConverter webRootReplacer, JavaCopyListConverter javaReplacer, ResourceCopyListConverter resourceReplacer, RmsCopyListConverter rmsCopyListConverter) {
        this.webRootReplacer = webRootReplacer;
        this.javaReplacer = javaReplacer;
        this.resourceReplacer = resourceReplacer;
        this.rmsCopyListConverter = rmsCopyListConverter;
    }

    /**
     * 获取路径替换器
     * @param relativePath 文件路径
     * @return 文件路径替换器
     */
    public BaseCopyListConverter getConverter(String relativePath){
        if(relativePath.startsWith(Const.WEB_ROOT_RMS_WEBAPP)){
            return rmsCopyListConverter;
        }else if(relativePath.startsWith(Const.WEB_ROOT)){
            return webRootReplacer;
        }else if(relativePath.endsWith(Const.JAVA)) {
            return javaReplacer;
        }else if(relativePath.startsWith(Const.PROPERTIES)){
            return resourceReplacer;
        }
        return null;
    }
}
