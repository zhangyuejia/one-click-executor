package com.zhangyj.cmdexecutor.core.common.handler.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.alibaba.fastjson.JSON;
import com.zhangyj.cmdexecutor.common.common.util.FileUtils;
import com.zhangyj.cmdexecutor.common.common.util.StrUtils;
import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import com.zhangyj.cmdexecutor.core.service.CmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 组件命令handler
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ComponentCmdHandler implements CmdHandler {

    private final ApplicationContext context;

    @Override
    public void handle(CmdExecConfig config, CmdLinePO cmdLinePo) throws Exception {
        log.info("cmd:" + JSON.toJSONString(cmdLinePo));
        CmdService cmdService = getCmdService(cmdLinePo);
        CmdConfig cmdConfig = getCmdConfig(cmdLinePo, cmdService);
        cmdService.exec(cmdConfig);
    }

    private CmdConfig getCmdConfig(CmdLinePO cmdLinePo, CmdService cmdService) {
        String configPath = FileUtils.getResourcePath("component/config/" + cmdLinePo.getCmd() + ".yml");
        return YamlUtil.loadByPath(configPath, cmdService.getConfigClass());
    }

    private CmdService getCmdService(CmdLinePO cmdLinePo) {
        String beanName = getBeanName(StrUtils.toCamel(cmdLinePo.getCmd()));
        if(!context.containsBean(beanName)){
            beanName = beanName + "Impl";
        }
        return SpringUtil.getBean(beanName);
    }

    private String getBeanName(String camelStr) {
        return "cmd" + camelStr + "Service";
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.COMPONENT;
    }
}
