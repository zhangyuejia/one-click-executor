package com.zhangyj.hostRefresher;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.zhangyj.common.cmd.RefreshDnsCmd;
import com.zhangyj.common.utils.CommandUtil;
import com.zhangyj.hostRefresher.config.HostsRefresherConfig;
import com.zhangyj.hostRefresher.pojo.HostsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class DoRefreshHosts {

    private final HostsRefresherConfig hostsRefresherConfig;

    @PostConstruct
    public void init(){
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
    }

    @Scheduled(cron = "${host-refresher.corn:0 */1 * * * ?}")
    public void checkNetworkTask(){
        try {
            List<HostsInfo> hosts = hostsRefresherConfig.getHosts();
            if(CollectionUtils.isEmpty(hosts)){
                return;
            }
            System.setProperty("http.proxyPort", "443");
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
            for (HostsInfo info : hosts) {
                log.info("读取{}文件路径：{}", info.getName(), info.getUrl());
                HttpRequest httpRequest = HttpUtil.createGet(info.getUrl());
                HttpResponse httpResponse = httpRequest.execute();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.bodyStream()))){
                    List<String> collect = reader.lines().collect(Collectors.toList());
                    FileUtil.writeLines(collect, hostsRefresherConfig.getHostsPath(), Charset.defaultCharset());
                }
            }
            // 刷新dns
            CommandUtil.exec(new RefreshDnsCmd().getCmd());
        }catch (Exception e){
            log.error("刷新hosts文件过程中发生异常", e);
        }
    }
}
