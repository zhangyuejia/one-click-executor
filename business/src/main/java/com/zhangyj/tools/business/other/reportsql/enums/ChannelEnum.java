package com.zhangyj.tools.business.other.reportsql.enums;

import lombok.Getter;

/**
 * @author zhangyj
 */

@Getter
public enum ChannelEnum {
    /**
     * SMS
     */
    SMS("短信", "1069211110"),
    /**
     * RMS
     */
    RMS("富信", "2021121601");

    ChannelEnum(String value, String spGate) {
        this.value = value;
        this.spGate = spGate;
    }

    private final String value;

    private final String spGate;

    public static ChannelEnum getByValue(String value){
        for (ChannelEnum item : ChannelEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}