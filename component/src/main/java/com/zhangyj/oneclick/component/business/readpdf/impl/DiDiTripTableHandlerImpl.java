package com.zhangyj.oneclick.component.business.readpdf.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.zhangyj.oneclick.component.business.readpdf.AbstractTripTableHandler;
import com.zhangyj.oneclick.component.entity.bo.ExpenseBO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangyj
 */
@Service
public class DiDiTripTableHandlerImpl extends AbstractTripTableHandler {

    @Override
    public boolean match(List<String> contents) {
        return match(contents, "滴滴");
    }

    @Override
    public String getStartSign() {
        return "序号";
    }

    @Override
    public ExpenseBO getTargetContent(String content) {
        // 1 滴滴快车 03-23 18:42 周四 宁德市 蕉城时代(3号门) 蕉城区|增坂村 21.1 60.58
        String[] split = StringUtils.split(content, " ");

        ExpenseBO expenseBO = new ExpenseBO();
        // 日期
        DateTime dateTime = DateUtil.parse(split[2] + " " + split[3], "MM-dd HH:mm");
        expenseBO.setDate(new Date(dateTime.getTime()));
        // 出发时间
        expenseBO.setStartTime(expenseBO.getDate());
        // 出发地
        expenseBO.setOrigin(split[split.length - 4]);
        // 目的地
        expenseBO.setDestination(split[split.length - 3]);
        // 结束时间
        expenseBO.setEndTime(DateUtils.addMinutes(expenseBO.getDate(), 40 + RandomUtil.getRandom().nextInt(-5, 5)));
        // 金额
        expenseBO.setMoney(split[split.length - 1]);

        return expenseBO;
    }
}
