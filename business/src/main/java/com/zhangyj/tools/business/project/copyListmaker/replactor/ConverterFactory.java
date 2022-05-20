package com.zhangyj.tools.business.project.copyListmaker.replactor;

import com.zhangyj.tools.common.constant.Const;
import com.zhangyj.tools.business.project.copyListmaker.config.Config;
import com.zhangyj.tools.business.project.copyListmaker.replactor.impl.JavaCopyListConverter;
import com.zhangyj.tools.business.project.copyListmaker.replactor.impl.ResourceCopyListConverter;
import com.zhangyj.tools.business.project.copyListmaker.replactor.impl.RmsCopyListConverter;
import com.zhangyj.tools.business.project.copyListmaker.replactor.impl.WebRootCopyListConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 路径替换器工厂
 * @author ZHANG
 */
@Component
@ConditionalOnBean(Config.class)
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
        if(StringUtils.isEmpty(relativePath)){
            return null;
        }
        if(relativePath.startsWith(Const.RMS_WEBAPP)){
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
