package com.zhangyj.tools.business.file.filesize.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhangyj
 */
@Data
@Builder
public class FileInfo {

    private String fileName;

    private Boolean isFile;

    private Long size;
}
