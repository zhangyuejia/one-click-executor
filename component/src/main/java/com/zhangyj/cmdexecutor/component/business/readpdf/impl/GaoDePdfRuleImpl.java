package com.zhangyj.cmdexecutor.component.business.readpdf.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.zhangyj.cmdexecutor.component.business.readpdf.BasePdfRule;
import com.zhangyj.cmdexecutor.component.entity.bo.ExpenseBO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GaoDePdfRuleImpl implements BasePdfRule {

    @Override
    public boolean match(List<String> contents) {
        return match(contents, "高德");
    }

    @Override
    public String getStartSign() {
        return "序号";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public ExpenseBO getTargetContent(String content) {
        // 1 及时用车 及时经济型 2023-03-22 17:06 宁德市 贵州习酒宁德体验馆(亿利城铂金公馆店)对面 增坂村(公交站) 30.0
        String[] split = StringUtils.split(content, " ");

        ExpenseBO expenseBO = new ExpenseBO();
        // 日期
        DateTime dateTime = DateUtil.parse(split[3].substring(5) + " " + split[4], "MM-dd HH:mm");
        expenseBO.setDate(new Date(dateTime.getTime()));
        // 出发时间
        expenseBO.setStartTime(expenseBO.getDate());
        // 出发地
        expenseBO.setOrigin(split[6]);
        // 目的地
        expenseBO.setDestination(split[7]);
        // 结束时间
        expenseBO.setEndTime(DateUtils.addMinutes(expenseBO.getDate(), 40 + RandomUtil.getRandom().nextInt(-5, 5)));
        // 金额
        expenseBO.setMoney(split[8]);
        return expenseBO;
    }
}
