package com.zhangyj.cmdexecutor.component.entity.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhangyj
 */
@Data
@Builder
public class FileInfoBO {

    private String fileName;

    private Boolean isFile;

    private Long size;
}
