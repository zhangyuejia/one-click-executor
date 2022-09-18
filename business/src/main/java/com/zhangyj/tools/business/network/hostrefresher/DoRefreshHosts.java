package com.zhangyj.tools.business.network.hostrefresher;


import cn.hutool.core.io.FileUtil;
import com.zhangyj.tools.business.network.hostrefresher.config.HostsRefresherConfig;
import com.zhangyj.tools.business.network.hostrefresher.pojo.HostsInfo;
import com.zhangyj.tools.common.base.AbstractRunner;
import com.zhangyj.tools.common.cmd.RefreshDnsCmd;
import com.zhangyj.tools.common.utils.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 刷新hosts文件
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(HostsRefresherConfig.class)
public class DoRefreshHosts extends AbstractRunner<HostsRefresherConfig> {

    @Scheduled(cron = "${host-refresher.corn:0 */1 * * * ?}")
    public void checkNetworkTask(){
        try {
            List<HostsInfo> hosts = config.getHosts();
            if(CollectionUtils.isEmpty(hosts)){
                return;
            }
            for (HostsInfo info : hosts) {
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
            CommandUtil.execCommand(Charset.defaultCharset(), new RefreshDnsCmd().getCmd());
        }catch (Exception e){
            log.error("刷新hosts文件过程中发生异常", e);
        }
    }

    @Override
    protected void doRun() {}
}
