package com.zhangyj.oneclick.component.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhangyj
 */
@Getter
@RequiredArgsConstructor
public enum FileSizeEnum {

    /**
     * B
     */
    B(1L),
    /**
     * KB
     */
    KB(FileSizeEnum.B.getUnit() * 1024L),
    /**
     * B
     */
    MB(FileSizeEnum.KB.getUnit() * 1024L),
    /**
     * KB
     */
    GB(FileSizeEnum.MB.getUnit() * 1024L);

    private final long unit;
}
