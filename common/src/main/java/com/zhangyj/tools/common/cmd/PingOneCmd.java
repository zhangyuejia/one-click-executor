package com.zhangyj.tools.common.cmd;

/**
 * ping百度一次命令
 * @author zhangyj
 */
public class PingOneCmd implements ICmd{

    @Override
    public String getCmd() {
        return "ping www.baidu.com -n 1";
    }
}
