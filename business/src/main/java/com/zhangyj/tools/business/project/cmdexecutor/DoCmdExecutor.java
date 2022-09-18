package com.zhangyj.tools.business.project.cmdexecutor;

import com.alibaba.excel.util.StringUtils;
import com.zhangyj.tools.business.project.cmdexecutor.config.CmdExecutorConfig;
import com.zhangyj.tools.common.base.AbstractRunner;
import com.zhangyj.tools.common.utils.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(CmdExecutorConfig.class)
public class DoCmdExecutor extends AbstractRunner<CmdExecutorConfig> {

    @Override
    protected void doRun() throws Exception {
        File file = new File(config.getExecPath());
        if(!file.exists()){
            boolean mkdirs = file.mkdirs();
            if(!mkdirs){
                throw new RuntimeException("创建路径失败：" + config.getExecPath());
            }
        }
        // 自定义编码，避免输出乱码
        Charset charset = config.getCharset() == null? StandardCharsets.UTF_8: Charset.forName(config.getCharset());
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(config.getCmdFilePath()), Charset.defaultCharset())){
            String line;
            while ((line = reader.readLine()) != null){
                if(StringUtils.isBlank(line)){
                    continue;
                }
                String[] cmdArr = line.split("\\$");
                String cmdDir = config.getExecPath() + (cmdArr.length>1? File.separator + cmdArr[1].trim(): "");
                 CommandUtil.execCommand(charset, cmdArr[0].trim(), cmdDir, log::info);
            }
        }
    }
}
