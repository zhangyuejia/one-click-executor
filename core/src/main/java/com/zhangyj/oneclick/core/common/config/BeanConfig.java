package com.zhangyj.oneclick.core.common.config;

import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhang.yuejia1
 */
@Configuration
public class BeanConfig {

    @Bean
    public Map<CmdTypeEnum, CmdHandler> cmdTypeHandlerMap(List<CmdHandler> list){
        return list.stream().collect(Collectors.toMap(CmdHandler::getCmdType, Function.identity()));
    }
}
