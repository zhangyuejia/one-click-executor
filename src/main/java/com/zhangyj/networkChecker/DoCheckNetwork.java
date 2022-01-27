package com.zhangyj.networkChecker;

import com.zhangyj.networkChecker.config.NetWorkCheckerConfig;
import com.zhangyj.utils.CommandUtil;
import com.zhangyj.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 拼接文件
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(NetWorkCheckerConfig.class)
public class DoCheckNetwork implements CommandLineRunner {

    private final NetWorkCheckerConfig netWorkCheckerConfig;

    private final static String CHECK_CMD = "ping www.baidu.com -n 1";

    private final static String CHECK_KEYWORD = "时间=";

    private final static String CONNECT_WIFI = "netsh wlan connect name=\"YJ&YR\" ssid=\"YJ&YR\"";

    @Override
    public void run(String... args) throws Exception {
        log.info("打开网络检测功能，检测间隔{}秒", netWorkCheckerConfig.getCheckPeriod());
        //noinspection InfiniteLoopStatement
        while (true){
            TimeUnit.SECONDS.sleep(netWorkCheckerConfig.getCheckPeriod());
            boolean networkEnable = checkNetwork();
            log.info("检测网络结果：" + (networkEnable? "正常":"断网"));
            if(!networkEnable){

                log.info("重新联网");
                reconnectWifi();
                TimeUnit.SECONDS.sleep(15);
                networkEnable = checkNetwork();
                log.info("再次检测网络结果：" + (networkEnable? "正常":"断网"));
                if(networkEnable){
                    continue;
                }
                log.info("重启网卡");
                restartNetWork();
                log.info("重启网卡成功，等待60秒");
                TimeUnit.SECONDS.sleep(60);
                networkEnable = checkNetwork();
                log.info("再次检测网络结果：" + (networkEnable? "正常":"断网"));
                if(networkEnable){
                    continue;
                }
                log.info("重启电脑，再见");
                execCommand();
            }
        }
    }

    private void reconnectWifi() {
        try {
            log.info("执行命令：{}", CONNECT_WIFI);
            execCmd(CONNECT_WIFI, "GBK");
        } catch (Exception e) {
            log.error("执行命令出现异常:" + CONNECT_WIFI, e);
        }
    }

    private boolean checkNetwork(){
        boolean networkEnable = false;
        try (BufferedReader reader = CommandUtil.getCommandReader(Charset.forName("GBK"), CHECK_CMD)){
            List<String> outputs = reader.lines().collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(outputs)){
                for (String output : outputs) {
                    if (output != null && output.contains(CHECK_KEYWORD)) {
                        networkEnable = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("校验网络出现异常", e);
        }
        return networkEnable;
    }

    private void restartNetWork(){

        String command = null;
        try {
            command = "netsh interface set interface "+netWorkCheckerConfig.getNetworkName()+" disabled";
            log.info("执行命令：{}", command);
            execCmd(command, "GBK");
            command = "netsh interface set interface "+netWorkCheckerConfig.getNetworkName()+" enable";
            log.info("执行命令：{}", command);
            execCmd(command, "GBK");
        } catch (Exception e) {
            log.error("执行命令出现异常:" + command, e);
        }
    }

    private void execCommand() {
        String command = netWorkCheckerConfig.getCommand();
        if(StringUtil.isEmpty(command)){
            return;
        }
        log.info("执行命令：{}", command);
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            log.error("执行命令出现异常:" + command, e);
        }
    }

    private void execCmd(String cmd, String charset){
        try (BufferedReader reader = CommandUtil.getCommandReader(Charset.forName(charset), cmd)){
            reader.lines().filter(StringUtil::isNotEmpty).forEach(log::info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
