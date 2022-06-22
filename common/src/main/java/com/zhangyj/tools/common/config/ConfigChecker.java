package com.zhangyj.tools.common.config;

import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置校验类
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigChecker implements InitializingBean {

    private final List<AbstractConfig> abstractConfigs;

    private final ToolsConfig toolsConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!toolsConfig.getCheckOneFunction()){
            return;
        }
        if(CollectionUtils.isEmpty(abstractConfigs)){
            log.info("开启配置个数为0");
        }
        if(abstractConfigs.size() > 1){
            throw new RuntimeException("不允许开启功能超过1个，开启的功能为：" + abstractConfigs.stream().map(AbstractConfig::getFunctionName).collect(Collectors.toList()));
        }
    }
}
