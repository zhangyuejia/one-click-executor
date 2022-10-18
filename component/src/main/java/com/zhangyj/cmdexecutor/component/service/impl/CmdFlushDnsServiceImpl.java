package com.zhangyj.cmdexecutor.component.service.impl;

import cn.hutool.core.io.FileUtil;
import com.zhangyj.cmdexecutor.component.common.config.CmdFlushDnsConfig;
import com.zhangyj.cmdexecutor.component.entity.bo.HostsInfoBO;
import com.zhangyj.cmdexecutor.core.common.handler.impl.CheckStringHandler;
import com.zhangyj.cmdexecutor.core.common.util.CommandUtils;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 刷新dns配置
 * @author zhangyj
 */
@Slf4j
@Service
public class CmdFlushDnsServiceImpl extends AbstractCmdService<CmdFlushDnsConfig> {

    @Override
    public void exec() throws Exception {
        List<HostsInfoBO> hosts = config.getHosts();
        if(CollectionUtils.isEmpty(hosts)){
            return;
        }
        for (HostsInfoBO info : hosts) {
            log.info("读取{}文件路径：{}", info.getName(), info.getUrl());
            URL url = new URL(info.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024)){
                List<String> collect = reader.lines().collect(Collectors.toList());
                FileUtil.writeLines(collect, config.getHostsPath(), Charset.defaultCharset());
            }
        }
        // 刷新dns
        CommandUtils.execCommand(cmdExecConfig.getCharset(), "ipconfig /flushdns", null, new CheckStringHandler(config));
    }
}
