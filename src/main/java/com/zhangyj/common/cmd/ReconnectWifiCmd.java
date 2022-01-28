package com.zhangyj.common.cmd;

import lombok.RequiredArgsConstructor;

/**
 * 重新连接wifi命令
 * @author zhangyj
 */
@RequiredArgsConstructor
public class ReconnectWifiCmd implements ICmd{

    private final String wifiName;

    @Override
    public String getCmd() {
        return "netsh wlan connect name=\"" + wifiName + "\" ssid=\"" + wifiName + "\"";
    }
}
