package com.zhangyj.oneclick.core.common.factory;

import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.util.EnumUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLinePO;
import com.zhangyj.oneclick.core.entity.bo.CmdNamePO;
import com.zhangyj.oneclick.core.entity.bo.CmdTypePO;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyj
 */
public class CmdLinePoFactory {

    /**
     * 命令cmd组成：命令类型,命令,命令执行路径,是否异步
     * 1.命令类型：shell|component|param [--参数=参数值]
     *  1.1 命令类型详见：{@link com.zhangyj.oneclick.core.common.enums.CmdTypeEnum}
     *  1.2 参数，根据命令类型的不同，有以下含义
     *   1.2.1 shell：见：{@link com.zhangyj.oneclick.core.common.enums.CmdShellParamaterEnum}
     * 2.命令，根据命令类型的不同，有以下含义
     *  2.1 shell：直接执行的命令
     *  2.2 component：组件名称（-转驼峰，具体实现对应AbstractCmdService实现类）[--参数=参数值]
     *   2.2.1：变量是用于替换yml配置的变量值，优先级为：yml < param全局变量 < 命令行参数
     *  2.3 param：set 全局变量名=变量值
     * 3.命令执行路径，根据命令类型的不同，有以下含义
     *  3.1 shell：命令执行路径
     *  3.2 component：配置文件路径
     */
    public static CmdLinePO newInstance(String cmdLine){
        String[] cmdLineArr = Arrays.copyOf(cmdLine.split(","), 4);
        CmdLinePO po = new CmdLinePO();
        po.setCmdType(getCmdType(cmdLineArr[0]));
        po.setCmdName(getCmdName(po.getCmdType(), cmdLineArr[1]));
        po.setDir(cmdLineArr[2]);
        po.setIsAsync(CoreConstant.ASYNC.equals(cmdLineArr[3]));
        return po;
    }

    private static CmdNamePO getCmdName(CmdTypePO cmdType, String str) {
        String[] split = str.split(" ");
        CmdNamePO po = new CmdNamePO();
        po.setValue(split[0].trim());
        if (split.length == 1 || !EnumUtils.equalsAny(cmdType.getValue(), CmdTypeEnum.PARAM, CmdTypeEnum.COMPONENT)) {
            return po;
        }
        String[] paramsArr = new String[split.length - 1];
        System.arraycopy(split, 1, paramsArr, 0, split.length - 1);
        po.setParamMap(paramArrToMap(paramsArr));
        return po;
    }

    private static CmdTypePO getCmdType(String str){
        String[] split = str.split(" ");
        CmdTypePO po = new CmdTypePO();
        po.setValue(split[0]);
        if(split.length == 1){
            po.setParamMap(Collections.emptyMap());
            return po;
        }
        String[] paramsArr = new String[split.length - 1];
        System.arraycopy(split, 1, paramsArr, 0, split.length - 1);
        po.setParamMap(paramArrToMap(paramsArr));
        return po;
    }

    private static Map<String, Object> paramArrToMap(String[] paramsArr) {
        Map<String, Object> paramMap = new HashMap<>(paramsArr.length - 1);
        for (String s : paramsArr) {
            String[] paramArr = s.split("=");
            String paramNameExp = paramArr[0].trim();
            if (!paramNameExp.startsWith(CoreConstant.CMD_PARAM_PREFIX)) {
                throw new RuntimeException("命令类型参数必须以" + CoreConstant.CMD_PARAM_PREFIX + "开头：" + paramNameExp);
            }
            paramMap.put(paramNameExp.substring(CoreConstant.CMD_PARAM_PREFIX.length()), paramArr[1].trim());
        }
        return paramMap;
    }
}
