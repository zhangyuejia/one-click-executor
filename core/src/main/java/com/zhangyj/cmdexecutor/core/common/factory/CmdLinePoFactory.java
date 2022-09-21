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
        if(cmdArr.length < LENGTH){
            cmdArr = Arrays.copyOf(cmdArr, LENGTH);
        }
        CmdLinePO po = new CmdLinePO();
        po.setType(cmdArr[0]);
        po.setCmd(cmdArr[1]);
        po.setDir(cmdArr[2]);
        return po;
    }
}
