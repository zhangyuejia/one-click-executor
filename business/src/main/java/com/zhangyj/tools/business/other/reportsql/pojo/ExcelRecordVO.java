package com.zhangyj.tools.business.other.reportsql.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangyj
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ExcelRecordVO {

    private String year;

    private String iYmd;

    private String iMonth;

    private String channelName;

    private String spGate;

    private String spId;

    private String iCount;

    private String rSucc;

    private String rFail;

    private String rNet;
}
