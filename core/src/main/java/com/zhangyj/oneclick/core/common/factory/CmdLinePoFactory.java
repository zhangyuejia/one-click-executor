package com.zhangyj.oneclick.core.common.factory;

import cn.hutool.core.util.StrUtil;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.enums.CmdTypeParamEnum;
import com.zhangyj.oneclick.core.common.util.EnumUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLineBo;
import com.zhangyj.oneclick.core.entity.bo.CmdNameBO;
import com.zhangyj.oneclick.core.entity.bo.CmdTypeBO;
import com.zhangyj.oneclick.core.entity.bo.CmdTypeParamBO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.zhangyj.oneclick.core.common.constant.CoreConstant.COMPONENT_CONFIG_PATH;

/**
 * @author zhangyj
 */
public class CmdLinePoFactory {

    /**
     * 命令cmd组成：命令类型,命令,命令执行路径,是否异步
     * 1.命令类型：shell|component|param [--参数=参数值]
     *  1.1 命令类型详见：{@link com.zhangyj.oneclick.core.common.enums.CmdTypeEnum}
     *  1.2 参数，根据命令类型的不同，有以下含义
     *   1.2.1 shell：见：{@link CmdTypeParamEnum}
     * 2.命令，根据命令类型的不同，有以下含义
     *  2.1 shell：直接执行的命令
     *  2.2 component：组件名称（-转驼峰，具体实现对应AbstractCmdService实现类）[--参数=参数值]
     *   2.2.1：变量是用于替换yml配置的变量值，优先级为：yml < param全局变量 < 命令行参数
     *  2.3 param：set 全局变量名=变量值
     * 3.命令执行路径，根据命令类型的不同，有以下含义
     *  3.1 shell：命令执行路径
     *  3.2 component：配置文件路径，可为空，默认读取#{[classpath]}component\config\下同名yml
     * 4.是否异步，值范围【async, sync（默认）】
     */
    public static CmdLineBo newInstance(CmdExecConfig config, String cmdLine){
        String[] cmdLineArr = Arrays.copyOf(cmdLine.split(config.getCmdSeparator()), 4);
        CmdLineBo po = new CmdLineBo();
        po.setOriginalValue(cmdLine);
        po.setCmdType(getCmdType(cmdLineArr[0]));
        po.setCmdName(getCmdName(po.getCmdType(), cmdLineArr[1]));
        po.setDir(getCmdDir(po.getCmdType(), po.getCmdName(), cmdLineArr[2]));
        po.setIsAsync(CoreConstant.ASYNC.equals(cmdLineArr[3]));
        return po;
    }

    private static String getCmdDir(CmdTypeBO cmdType, CmdNameBO cmdName, String cmdDir) {
        if (StrUtil.isBlank(cmdDir) && CmdTypeEnum.COMPONENT.equals(cmdType.getCmdType())) {
            return StrUtils.parseTplContent(COMPONENT_CONFIG_PATH, CmdExecConfig.PARAM_MAP) + cmdName.getValue() + ".yml";
        }
        return cmdDir;
    }

    private static CmdNameBO getCmdName(CmdTypeBO cmdType, String str) {
        String[] split = str.split(" ");
        CmdNameBO po = new CmdNameBO();
        po.setOriginalValue(str);
        if (split.length == 1 || !EnumUtils.equalsAny(cmdType.getCmdType(), CmdTypeEnum.PARAM, CmdTypeEnum.COMPONENT)) {
            po.setValue(str);
            return po;
        }
        String[] paramsArr = new String[split.length - 1];
        po.setValue(split[0].trim());
        System.arraycopy(split, 1, paramsArr, 0, split.length - 1);
        po.setParamMap(paramArrToMap(paramsArr));
        return po;
    }

    private static CmdTypeBO getCmdType(String str){
        String[] split = str.split(" ");
        CmdTypeBO po = new CmdTypeBO();
        po.setOriginalValue(str);
        po.setCmdType(EnumUtils.getByValue(CmdTypeEnum.class, split[0]));
        po.setCmdTypeParam(CmdTypeParamBO.newDefault());
        if(split.length == 1){
            return po;
        }
        CmdTypeParamBO cmdTypeParam = getCmdTypeParamBO(split);
        po.setCmdTypeParam(cmdTypeParam);
        return po;
    }

    private static CmdTypeParamBO getCmdTypeParamBO(String[] split) {
        String[] paramsArr = new String[split.length - 1];
        System.arraycopy(split, 1, paramsArr, 0, split.length - 1);

        Map<String, Object> paramMap = paramArrToMap(paramsArr);
        CmdTypeParamBO cmdTypeParam = CmdTypeParamBO.newDefault();
        cmdTypeParam.putAll(paramMap);
        return cmdTypeParam;
    }

    private static Map<String, Object> paramArrToMap(String[] paramsArr) {
        Map<String, Object> paramMap = new HashMap<>(paramsArr.length - 1);
        for (String s : paramsArr) {
            String[] paramArr = Arrays.copyOf(s.split("="), 2);
            String paramNameExp = paramArr[0].trim();
            if (!paramNameExp.startsWith(CoreConstant.PREFIX_CMD_PARAM)) {
                throw new RuntimeException("命令类型参数必须以" + CoreConstant.PREFIX_CMD_PARAM + "开头：" + paramNameExp);
            }
            if (paramArr[1] == null) {
                paramArr[1] = StrUtil.EMPTY;
            }
            paramMap.put(paramNameExp.substring(CoreConstant.PREFIX_CMD_PARAM.length()), paramArr[1].trim());
        }
        return paramMap;
    }
}
