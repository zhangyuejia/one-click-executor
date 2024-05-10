package com.zhangyj.oneclick.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
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
import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdExecServiceImpl extends AbstractCmdService<CmdExecConfig> implements CmdExecService {

    private final List<CmdHandler> cmdHandlers;

    @Override
    public void exec() throws Exception {
        // 初始化
        initConfig();
        // CMD变量
        initParameter();
        String filePath = getExecFilePath();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())){
            String line;
            while ((line = reader.readLine()) != null){
                for (CmdHandler cmdHandler : cmdHandlers) {
                    line = line.trim();
                    if (cmdHandler.match(line)) {
                        String content = StrUtils.parseTplContent(line, CmdExecConfig.PARAM_MAP);
                        cmdHandler.handle(config, content);
                    }
                }
            }
        }
    }

    private String getExecFilePath() {
        if(StringUtils.isBlank(config.getShellPath())) {
            throw new IllegalArgumentException("执行" + this.getDesc() + "报错，配置项[shellPath]不能为空");
        }
        if(new File(config.getShellPath()).exists()){
            return config.getShellPath();
        }else {
            return FileUtils.getResourcePath(config.getShellPath());
        }
    }

    private void initConfig() {
        if (StringUtils.isBlank(config.getDir())) {
            config.setDir(cmdExecConfig.getDir());
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

    private void initParameter() {
        Map<String, String> paramMap = CmdExecConfig.PARAM_MAP;
        paramMap.put("dir", config.getDir());
        paramMap.put("classpath", FileUtils.getResourcePath());
        log.info("初始化变量：{}", JSON.toJSONString(paramMap));
    }

    @Override
    public String getDesc() {
        return "命令执行功能";
    }
}
