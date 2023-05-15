package com.hss.kgdoctor.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatTypeVerificationUtil {

    /**
     * 功能描述：枚举：聊天信息的类型
     * @author RenShiWei
     * Date: 2020/2/6 15:58
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum ChatMessageTypeEnum {
        /**文本*/
        TEXT("text"),
        /**图片*/
        IMAGE("image"),
        /**音频*/
        VOICE("voice"),
        /**心跳包*/
        HEART("heart"),
        ;
        private String chatType;
    }

    /**
     * 功能描述：
     * @param chatType 预判断类型
     * @return boolean
     */
    public static boolean verifyChatType(String chatType) {
        //循环枚举
        for (ChatMessageTypeEnum airlineTypeEnum : ChatMessageTypeEnum.values()) {
            if (StringUtils.isNotBlank(chatType)&&chatType.equals(airlineTypeEnum.getChatType())){
                return true;
            }
        }
        return false;
    }

}
