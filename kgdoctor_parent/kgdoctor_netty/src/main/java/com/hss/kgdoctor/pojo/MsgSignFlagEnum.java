package com.hss.kgdoctor.pojo;

import lombok.Getter;

public enum MsgSignFlagEnum {
    /** 消息是否签收 */
    unsign(0,"未签收"),
    signed(1,"已签收");

    @Getter
    public final int type;
    @Getter
    public final String value;

    private MsgSignFlagEnum(int type,String value) {
        this.type = type;
        this.value = value;
    }
    
}
