package com.zhangyj.common.cmd;

import com.zhangyj.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;

/**
 * get请求curl
 * @author zhangyj
 */
@RequiredArgsConstructor
public class CurlGetCmd implements ICmd{

    private final String url;

    private final String params;

    @Override
    public String getCmd() {
        return "curl " + (StringUtil.isEmpty(params)? "": params) + " -X GET " + url;
    }
}
