package com.zhangyj.oneclick.core.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.factory.CmdLinePoFactory;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.common.util.EnumUtils;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLineBo;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import com.zhangyj.oneclick.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdExecServiceImpl extends AbstractCmdService<CmdExecConfig> implements CmdExecService {

    private final Map<CmdTypeEnum, CmdHandler> cmdTypeHandlerMap;

    @Override
    public void exec(CmdExecConfig config) throws Exception {
        // 初始化
        initConfig(config);
        // cmd变量
        initParameter(config);
        String filePath = getExecFilePath(config);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())){
            String fileLine;
            while ((fileLine = reader.readLine()) != null){
                if (StrUtil.isBlank(fileLine) || fileLine.startsWith("#")) {
                    continue;
                }
                String[] lineSplit = fileLine.split("&&");
                for (String line : lineSplit) {
                    line = line.trim();
                    String cmdType = line.substring(0, line.indexOf(config.getCmdSeparator())).split(" ")[0];
                    CmdHandler cmdHandler = cmdTypeHandlerMap.get(EnumUtils.getByValue(CmdTypeEnum.class, cmdType));
                    String cmdLine = StrUtils.parseTplContent(line, CmdExecConfig.PARAM_MAP);
                    log.info("命令处理器[{}]开始执行：{}", cmdHandler.getCmdType().getCode(), cmdLine);
                    CmdLineBo cmdLineBo = CmdLinePoFactory.newInstance(config, cmdLine);
                    log.info("命令解析完毕：{}", JSONUtil.toJsonStr(cmdLineBo));
                    config.setCmdLineBo(cmdLineBo);
                    cmdHandler.handle(config);
                }
            }
        }
    }

    private String getExecFilePath(CmdExecConfig config) {
        if(StringUtils.isBlank(config.getShellPath())) {
            throw new IllegalArgumentException("执行" + this.getDesc() + "报错，配置项[shellPath]不能为空");
        }
        if(new File(config.getShellPath()).exists()){
            return config.getShellPath();
        }else {
            return FileUtils.getResourcePath(config.getShellPath());
        }
    }

    private void initConfig(CmdExecConfig config) {
        if (StringUtils.isBlank(config.getDir())) {
            config.setDir(cmdExecConfig.getDir());
        }
        if (StringUtils.isBlank(config.getCmdSeparator())) {
            config.setCmdSeparator(cmdExecConfig.getCmdSeparator());
        }
        // 默认utf-8编码
        if (config.getCharset() == null) {
            config.setCharset(StandardCharsets.UTF_8.toString());
        }
        // 创建命令执行路径
        File file = new File(config.getDir());
        if(!file.exists()){
            boolean mkdir = file.mkdirs();
            if(!mkdir){
                throw new RuntimeException("创建路径失败：" + config.getDir());
            }
        }
    }

    private void initParameter(CmdExecConfig config) {
        Map<String, Object> paramMap = CmdExecConfig.PARAM_MAP;
        paramMap.put("dir", config.getDir());
        paramMap.put("classpath", FileUtils.getResourcePath());
        log.info("初始化变量：{}", JSON.toJSONString(paramMap));
    }

    @Override
    public String getDesc() {
        return "命令执行功能";
    }
}
