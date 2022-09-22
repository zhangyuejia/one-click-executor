package com.zhangyj.cmdexecutor.core.common.factory;

import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;

import java.util.Arrays;

/**
 * @author zhangyj
 */
public class CmdLinePoFactory {

    private final static int LENGTH = 3;

    public static CmdLinePO newInstance(String cmdLine){
        String[] cmdArr = cmdLine.split(",");
        if(cmdArr.length != LENGTH){
            throw new RuntimeException("非法命令,格式为 [类型],[命令],[路径]:" + cmdLine);
        }
        CmdLinePO po = new CmdLinePO();
        po.setType(cmdArr[0]);
        po.setCmd(cmdArr[1]);
        po.setDir(cmdArr[2]);
        return po;
    }
}
