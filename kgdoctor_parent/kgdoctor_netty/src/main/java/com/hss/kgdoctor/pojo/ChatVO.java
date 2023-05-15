package com.hss.kgdoctor.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ChatVO {
    // 消息ID
    private Integer questionId;

    // 消息类型
    private String chatMessageType;

    // 内容
    private String chatMessage;

    // 发送方ID
    private Integer fromUserId;

    // 接收方ID
    private Integer toUserId;

    // 消息事件
    private Date dateTime;
}
