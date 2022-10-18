package com.zhangyj.cmdexecutor.core.common.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdTypePO;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdTypeParameterPO;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyj
 */
public class CmdLinePoFactory {

    public static CmdLinePO newInstance(String cmdLine){
        String[] cmdArr = Arrays.copyOf(cmdLine.split(","), 3);
        CmdLinePO po = new CmdLinePO();
        po.setCmdType(getCmdType(cmdArr[0]));
        po.setCmd(cmdArr[1]);
        po.setDir(cmdArr[2]);
        return po;
    }

    private static CmdTypePO getCmdType(String str){
        String[] split = Arrays.copyOf(str.split(" "), 2);
        CmdTypePO cmdType = new CmdTypePO();
        cmdType.setType(split[0]);
        if(StringUtils.isBlank(split[1])){
            cmdType.setCmdTypeParameter(new CmdTypeParameterPO());
            return cmdType;
        }
        Map<String, Object> paramMap = new HashMap<>(2);
        for (int i = 1; i < split.length; i++) {
            String[] paramArr = Arrays.copyOf(split[i].split("="), 2);
            paramMap.put(paramArr[0].substring(1), paramArr[1]);
        }
        cmdType.setCmdTypeParameter(JSONObject.parseObject(JSON.toJSONString(paramMap), CmdTypeParameterPO.class));
        return cmdType;
    }
}
