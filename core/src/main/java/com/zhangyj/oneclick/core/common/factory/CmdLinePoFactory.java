package com.zhangyj.oneclick.core.common.factory;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.entity.bo.CmdLinePO;
import com.zhangyj.oneclick.core.entity.bo.CmdTypePO;
import com.zhangyj.oneclick.core.entity.bo.CmdTypeParameterPO;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyj
 */
public class CmdLinePoFactory {

    public static CmdLinePO newInstance(String cmdLine){
        String[] cmdLineArr = Arrays.copyOf(cmdLine.split(","), 4);
        CmdTypePO cmdType = getCmdType(cmdLineArr[0]);


        CmdLinePO po = new CmdLinePO();
        po.setCmdType(cmdType);
        po.setDir(cmdLineArr[2]);
        po.setIsAsync(CoreConstant.ASYNC.equals(cmdLineArr[3]));
        String[] cmdArr = cmdLineArr[1].split(" ");
        if (CmdTypeEnum.COMPONENT.getValue().equalsIgnoreCase(cmdType.getType())) {
            po.setCmd(cmdArr[0]);
        }else {
            po.setCmd(cmdLineArr[1]);
        }
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
