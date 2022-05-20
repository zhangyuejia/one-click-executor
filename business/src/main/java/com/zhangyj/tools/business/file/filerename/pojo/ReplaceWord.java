package com.zhangyj.tools.business.file.filerename.pojo;

import lombok.Data;

/**
 * @author zhangyj
 */
@Data
public class ReplaceWord {
    /**
     * 关键字
     */
    private String word;

    /**
     * 新关键字
     */
    private String newWord;
}
