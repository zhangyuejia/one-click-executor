package com.zhangyj.cmdexecutor.core.common.handler.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.factory.CmdLinePoFactory;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.common.util.StrUtils;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import com.zhangyj.cmdexecutor.core.service.CmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 组件命令handler
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdComponentHandler implements CmdHandler {

    private final ApplicationContext context;

    private final static String BASE_PACKAGE;

    static {
        // 扫描的根目录包（com.xxx）
        String aPackage = ClassUtil.getPackage(CmdComponentHandler.class);
        BASE_PACKAGE  = aPackage.substring(0, aPackage.indexOf(".", aPackage.indexOf(".") + 1));
    }

    @Override
    public void handle(CmdExecConfig config, String cmdLine) throws Exception {
        log.info("解析命令:" + cmdLine);
        CmdLinePO cmdLinePo = CmdLinePoFactory.newInstance(cmdLine);
        CmdService<?> cmdService = getCmdService(cmdLinePo);
        AbstractCmdConfig cmdConfig = getCmdConfig(cmdLinePo);
        ReflectUtil.invoke(cmdService, "setConfig", cmdConfig);
        ReflectUtil.invoke(cmdService, "exec");
    }

    private AbstractCmdConfig getCmdConfig(CmdLinePO cmdLinePo) {
        return (AbstractCmdConfig) YamlUtil.loadByPath(cmdLinePo.getDir(), getConfigClass(cmdLinePo.getCmd()));
    }

    private Class<?> getConfigClass(String cmd) {
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(BASE_PACKAGE, AbstractCmdConfig.class);
        for (Class<?> aClass : classes) {
            if(StringUtils.containsIgnoreCase(aClass.getSimpleName(), cmd.replace("-", ""))){
                return aClass;
            }
        }
        return null;
    }

    private CmdService<?> getCmdService(CmdLinePO cmdLinePo) {
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
