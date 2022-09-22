package com.zhangyj.cmdexecutor.core.service.impl;

import com.zhangyj.cmdexecutor.core.common.util.FileUtils;
import com.zhangyj.cmdexecutor.core.common.util.StrUtils;
import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdExecParameterPO;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import com.zhangyj.cmdexecutor.core.service.CmdExecService;
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

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdExecServiceImpl extends AbstractCmdService implements CmdExecService {

    private final List<CmdHandler> cmdHandlers;

    @Override
    public void exec(CmdConfig c) throws Exception {
        CmdExecConfig config = getConfig(c);
        // 初始化
        init(config);
        // CMD变量
        CmdExecParameterPO cmdParameter = getCmdExecParameter(config);
        String filePath = getExecFilePath(config);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())){
            String line;
            while ((line = reader.readLine()) != null){
                for (CmdHandler cmdHandler : cmdHandlers) {
                    if (cmdHandler.match(line)) {
                        String content = StrUtils.parseTplContent(line, cmdParameter);
                        cmdHandler.handle(config, content);
                    }
                }
            }
        }
    }

    private String getExecFilePath(CmdExecConfig config) {
        if(StringUtils.isNotBlank(config.getShellPath())){
            if(new File(config.getShellPath()).exists()){
                return config.getShellPath();
            }else if(FileUtils.existsResource(config.getShellPath())){
                return FileUtils.getResourcePath(config.getShellPath());
            }else {
                throw new IllegalArgumentException("执行" + config.getDesc() + "报错，文件路径不存在:" + config.getShellPath());
            }
        }else {
            if(cmdExecConfig.getBootLoad()){
                return FileUtils.getResourcePath("cmd.sh");
            }else {
                throw new IllegalArgumentException("执行" + config.getDesc() + "报错，配置项[shellPath]不能为空");
            }
        }
    }

    private CmdExecConfig getConfig(CmdConfig c) {
        CmdExecConfig config = (CmdExecConfig) c;
        if (!Boolean.TRUE.equals(config.getBootLoad())) {
            if (StringUtils.isBlank(config.getDir())) {
                config.setDir(cmdExecConfig.getDir());
            }
        }
        return config;
    }

    @Override
    public Class<? extends CmdConfig> getConfigClass() {
        return CmdExecConfig.class;
    }

    private void init(CmdExecConfig config) {
        // 默认utf-8编码
        if (config.getCharset() == null) {
            config.setCharset(StandardCharsets.UTF_8.toString());
        }
        // 创建命令执行路径
        File file = new File(config.getDir());
        if(!file.exists()){
            boolean mkdirs = file.mkdirs();
            if(!mkdirs){
                throw new RuntimeException("创建路径失败：" + config.getDir());
            }
        }
    }

    private CmdExecParameterPO getCmdExecParameter(CmdExecConfig config) {
        CmdExecParameterPO cmdParameter = new CmdExecParameterPO();
        cmdParameter.setDir(config.getDir());
        return cmdParameter;
    }
}
