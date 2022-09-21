package com.zhangyj.cmdexecutor.core.service.impl;

import com.zhangyj.cmdexecutor.common.common.util.FileUtils;
import com.zhangyj.cmdexecutor.common.common.util.StrUtils;
import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.factory.CmdLinePoFactory;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdExecParameterPO;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import com.zhangyj.cmdexecutor.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
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
@Service
@RequiredArgsConstructor
public class CmdExecServiceImpl implements CmdExecService {

    private final List<CmdHandler> cmdHandlers;

    @Override
    public void exec(CmdConfig c) throws Exception {
        CmdExecConfig config = (CmdExecConfig) c;
        // 初始化
        init(config);
        // CMD变量
        CmdExecParameterPO cmdParameter = getCmdExecParameter(config);
        String filePath = StringUtils.defaultIfBlank(config.getFilePath(), FileUtils.getResourcePath("cmd.sh"));
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())){
            String line;
            while ((line = reader.readLine()) != null){
                for (CmdHandler cmdHandler : cmdHandlers) {
                    if (cmdHandler.match(line)) {
                        CmdLinePO cmdLinePo = CmdLinePoFactory.newInstance(StrUtils.parseTplContent(line, cmdParameter));
                        cmdHandler.handle(config, cmdLinePo);
                    }
                }
            }
        }
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
