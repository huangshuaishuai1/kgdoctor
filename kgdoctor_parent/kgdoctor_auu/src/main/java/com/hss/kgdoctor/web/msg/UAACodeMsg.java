package com.hss.kgdoctor.web.msg;

import com.hss.kgdoctor.common.web.CodeMsg;

/**
 * Created by wolfcode-lanxw
 */
public class UAACodeMsg extends CodeMsg {
    private UAACodeMsg(Integer code, String msg){
        super(code,msg);
    }
    public static final UAACodeMsg LOGIN_ERROR = new UAACodeMsg(500101,"账号密码有误");
}
