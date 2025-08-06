package com.zhangyj.oneclick.core.entity.bo;

import com.zhangyj.oneclick.core.common.enums.CmdTypeParamEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令类型解析对象
 * @author zhangyj
 */
@Getter
@Setter
@ToString
public class CmdTypeParamBO {

    private final Map<String, Object> data = new HashMap<>();

    public static CmdTypeParamBO newDefault() {
        CmdTypeParamBO typeParamBO = new CmdTypeParamBO();
        for (CmdTypeParamEnum paramEnum : CmdTypeParamEnum.values()) {
            typeParamBO.getData().put(paramEnum.getCode(), paramEnum.getDefValue());
        }
        return typeParamBO;
    }

    public Boolean getBoolean(CmdTypeParamEnum typeParamEnum) {
        return Boolean.TRUE.toString().equals(this.data.get(typeParamEnum.getCode()).toString());
    }

    public void putAll(Map<String, Object> data) {
        this.data.putAll(data);
    }
}
