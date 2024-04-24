package com.zhangyj.oneclick.component.business.readpdf;

import com.zhangyj.oneclick.component.entity.bo.ExpenseBO;

import java.util.List;

/**
 * @author zhangyj
 */
public interface ITripTableHandler {

    /**
     * 是否匹配当前规则
     * @param contents 内容
     * @return 是否匹配
     */
    boolean match(List<String> contents);

    /**
     * 获取内容开始标志
     * @return 内容开始标志
     */
    String getStartSign();

    List<ExpenseBO> getTargetContents(List<String> contents);
}
