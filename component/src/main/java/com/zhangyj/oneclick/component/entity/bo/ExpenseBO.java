package com.zhangyj.oneclick.component.entity.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 费用BO
 * 日期
 * 地点	时间	地点	时间	单程价	人数	金额
 */
@Data
public class ExpenseBO {

    /**
     * 日期
     */
    @JSONField(format="MM.dd")
    private Date date;

    /**
     * 出发时间
     */
    @JSONField(format="HH:mm")
    private Date startTime;

    /**
     * 出发地
     */
    private String origin;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出发时间
     */
    @JSONField(format="HH:mm")
    private Date endTime;

    /**
     * 单程价
     */
    private String money;
}
