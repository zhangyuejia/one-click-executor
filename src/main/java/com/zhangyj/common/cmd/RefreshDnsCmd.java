package com.zhangyj.common.cmd;

/**
 * @author zhangyj
 */
public class RefreshDnsCmd implements ICmd{

    @Override
    public String getCmd() {
        return "ipconfig /flushdns";
    }
}
