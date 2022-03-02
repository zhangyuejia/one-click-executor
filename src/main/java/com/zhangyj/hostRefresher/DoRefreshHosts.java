package com.zhangyj.hostRefresher;


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

import java.util.List;

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

    @Scheduled(cron = "${host-refresher.corn:0 */1 * * * ?}")
    public void checkNetworkTask(){
        try {
            List<HostsInfo> hosts = hostsRefresherConfig.getHosts();
            if(CollectionUtils.isEmpty(hosts)){
                return;
            }
            for (HostsInfo info : hosts) {
                log.info("读取{}文件路径：{}", info.getName(), info.getUrl());
                HttpRequest httpRequest = HttpUtil.createGet(info.getUrl());
                httpRequest.timeout(10 * 1000);
                HttpResponse httpResponse = httpRequest.execute();
                httpResponse.writeBody(hostsRefresherConfig.getHostsPath());
            }
            // 刷新dns
            CommandUtil.exec(new RefreshDnsCmd().getCmd());
        }catch (Exception e){
            log.error("刷新hosts文件过程中发生异常", e);
        }
    }
}
