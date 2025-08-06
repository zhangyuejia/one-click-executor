package com.zhangyj.oneclick.core.entity.bo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhang.yuejia1
 */
@Getter
@RequiredArgsConstructor
public class CmdProcessBo {

    private final String cmd;

    private final String dir;

    private final Process process;
}
