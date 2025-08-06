package com.zhangyj.oneclick.core.common.handler.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.common.runner.CmdExecRunner;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLineBo;
import com.zhangyj.oneclick.core.service.CmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zhangyj.oneclick.core.common.constant.CoreConstant.PARAM_DIR;


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
    public void handle(CmdExecConfig config) {
        CmdLineBo cmdLineBo = config.getCmdLineBo();
        CmdService<?> cmdService = getCmdService(cmdLineBo);
        log.info(MessageFormatter.format(CoreConstant.CMD_LOG_BEFORE, cmdService.getDesc()).getMessage());

        AbstractCmdConfig cmdConfig = getCmdConfig(cmdLineBo);
        Assert.notNull(cmdConfig, "配置文件至少需要包含一个配置项：" + cmdLineBo.getDir());
        if (CollectionUtil.isNotEmpty(cmdLineBo.getCmdName().getParamMap())) {
            for (Map.Entry<String, Object> entry : cmdLineBo.getCmdName().getParamMap().entrySet()) {
                Field field = ReflectUtil.getField(cmdConfig.getClass(), entry.getKey());
                Assert.notNull(field, "类"  + cmdConfig.getClass() + "不存在属性" + entry.getKey());
                Object fieldValue = ReflectUtil.getFieldValue(cmdConfig, field);
                ReflectUtil.setFieldValue(cmdConfig, entry.getKey(), getValueAsType(entry.getValue().toString(), field));
                log.info("更新组件配置参数[{}]：{}->{}", entry.getKey(), fieldValue, entry.getValue());
            }
        }
        if (StrUtil.isNotBlank(cmdConfig.getDir())) {
            String value = cmdConfig.getDir().trim();
            CmdExecConfig.PARAM_MAP.put(PARAM_DIR, value);
            log.info("更新全局变量[{}]：{}, 变量集合：{}", PARAM_DIR, value, JSONUtil.toJsonStr(CmdExecConfig.PARAM_MAP));
        }
        cmdConfig.setCmdLineBo(cmdLineBo);
        ReflectUtil.invoke(cmdService, "setConfig", cmdConfig);
        log.info("开始执行组件[{}]，配置：{}", cmdLineBo.getCmdName().getValue(), JSONUtil.toJsonStr(cmdConfig));
        CmdExecRunner.execTask(cmdLineBo.getIsAsync(), () -> ReflectUtil.invoke(cmdService, "exec"));
    }

    private Object getValueAsType(String value, Field field) {
        if (value.startsWith("{") && value.endsWith("}")) {
            return BeanUtil.toBean(value, field.getType());
        }else if(value.startsWith("[") && value.endsWith("]")) {
            return JSONUtil.toBean(value, field.getGenericType(), false);
        }else {
            return value;
        }
    }

    private AbstractCmdConfig getCmdConfig(CmdLineBo cmdLineBo) {
        List<String> list = FileUtil.readLines(cmdLineBo.getDir(), Charset.defaultCharset());
        String cmdNameValue = cmdLineBo.getCmdName().getValue();
        String tmpFilePath = FileUtils.getTempDir("tmpYml") + File.separator + cmdNameValue + "-" + System.currentTimeMillis() + ".yaml";
        FileUtil.writeLines(list.stream().map(v ->
                        StrUtils.parseTplContent(v, CmdExecConfig.PARAM_MAP)).collect(Collectors.toList()),
                tmpFilePath, Charset.defaultCharset());
        AbstractCmdConfig cmdConfig = (AbstractCmdConfig) YamlUtil.loadByPath(tmpFilePath, getConfigClass(cmdNameValue));
        FileUtil.del(tmpFilePath);
        return cmdConfig;
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

    private CmdService<?> getCmdService(CmdLineBo cmdLineBo) {
        String beanName = getBeanName(StrUtils.toCamel(cmdLineBo.getCmdName().getValue()));
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
