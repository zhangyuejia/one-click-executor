package com.zhangyj.networkChecker;


import com.zhangyj.common.cmd.PingOneCmd;
import com.zhangyj.common.cmd.ReconnectWifiCmd;
import com.zhangyj.common.constant.CharSets;
import com.zhangyj.common.utils.CommandUtil;
import com.zhangyj.common.utils.ThreadUtils;
import com.zhangyj.networkChecker.config.NetWorkCheckerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 拼接文件
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(NetWorkCheckerConfig.class)
public class DoCheckNetwork {

    private final NetWorkCheckerConfig netWorkCheckerConfig;

    @Scheduled(cron = "${network-checker.corn:0/60 * * * * ?}")
    public void checkNetworkTask(){
        try {
            if(checkNetwork()){
                return;
            }
            log.info("重新连接网络，Wifi：{}", netWorkCheckerConfig.getWifiName());
            reconnectNetwork();
            ThreadUtils.sleepQuiet(20 * 1000L);
            checkNetwork();
        }catch (Exception e){
            log.error("网络校验过程中发生异常", e);
        }
    }

    private void reconnectNetwork() throws Exception {
        List<String> commandOutput = CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new ReconnectWifiCmd(netWorkCheckerConfig.getWifiName()).getCmd());
        if(CollectionUtils.isEmpty(commandOutput)){
            return;
        }
        commandOutput.forEach(log::info);
    }

    private boolean checkNetwork() throws Exception {
        boolean networkWorked = isNetworkWorked();
        log.info("检测网络结果：" + (networkWorked? "正常":"断网"));
        return networkWorked;
    }

    private boolean isNetworkWorked() throws Exception {
        List<String> commandOutput = CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new PingOneCmd().getCmd());
        if(CollectionUtils.isEmpty(commandOutput)){
            return false;
        }
        String keyword = "时间=";
        for (String output : commandOutput) {
            if (output != null && output.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
