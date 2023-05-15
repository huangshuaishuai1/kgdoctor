package com.hss.kgdoctor.pojo;

import lombok.Data;

@Data
public class ChatMsgVO extends ChatVO{

    private Integer action;
    //消息签收状态
    private MsgSignFlagEnum signed;
}
