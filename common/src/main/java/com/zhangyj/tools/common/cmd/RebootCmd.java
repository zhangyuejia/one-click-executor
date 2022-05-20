package com.zhangyj.tools.common.cmd;

/**
 * 重启电脑
 * @author zhangyj
 */
public class RebootCmd implements ICmd{

    @Override
    public String getCmd() {
        return "shutdown -r -t 0";
    }
}
