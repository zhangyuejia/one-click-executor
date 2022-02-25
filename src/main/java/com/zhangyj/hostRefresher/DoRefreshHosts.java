package com.zhangyj.hostRefresher;


import cn.hutool.core.io.FileUtil;
import com.zhangyj.common.cmd.RefreshDnsCmd;
import com.zhangyj.common.utils.CommandUtil;
import com.zhangyj.common.utils.UrlUtils;
import com.zhangyj.hostRefresher.config.HostsRefresherConfig;
import com.zhangyj.hostRefresher.pojo.HostsInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
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

                URL url = new URL(info.getUrl());
                final URLConnection conn = url.openConnection();
                conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
                conn.setConnectTimeout(10 * 1000);
//                try (BufferedReader reader = UrlUtils.getReader(info.getUrl())){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
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

    public static void main(String[] args) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);

        SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket();

        String[] protocols = socket.getSupportedProtocols();

        System.out.println("Supported Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }

        protocols = socket.getEnabledProtocols();

        System.out.println("Enabled Protocols: " + protocols.length);
        for (int i = 0; i < protocols.length; i++) {
            System.out.println(" " + protocols[i]);
        }
    }
}
