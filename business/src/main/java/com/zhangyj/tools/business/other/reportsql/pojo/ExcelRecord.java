package com.zhangyj.tools.business.other.reportsql.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangyj
 */
@Getter
@Setter
@EqualsAndHashCode
public class ExcelRecord {

    @ExcelProperty(index = 0)
    private String date;

    @ExcelProperty(index = 1)
    private String channelTypeZh;

    @ExcelProperty(index = 2)
    private String spId;

    @ExcelProperty(index = 3)
    private String iCount;

    @ExcelProperty(index = 4)
    private String rSucc;

    @ExcelProperty(index = 5)
    private String rFail;

    @ExcelProperty(index = 6)
    private String rNet;
}
