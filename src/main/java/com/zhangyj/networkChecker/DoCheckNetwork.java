package com.zhangyj.networkChecker;


import com.zhangyj.common.cmd.PingOneCmd;
import com.zhangyj.common.cmd.RebootCmd;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

    private LocalDateTime offNetworkTime;

    @Scheduled(cron = "${network-checker.corn:0 */1 * * * ?}")
    public void checkNetworkTask(){
        try {
            if(checkNetwork()){
                return;
            }
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
        if(networkWorked){
            offNetworkTime = null;
        }else {
            if(offNetworkTime == null){
                offNetworkTime = LocalDateTime.now();
            }else {
                // 如果断网事件超过30分钟，电脑将重启
                long offNetworkMinutes = ChronoUnit.MINUTES.between(offNetworkTime, LocalDateTime.now());
                if(offNetworkMinutes >= 30){
                    log.info("断网时间为：" + offNetworkMinutes + "分钟，即将重启电脑");
                    CommandUtil.exec(new RebootCmd().getCmd());
                }
            }

        }
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
