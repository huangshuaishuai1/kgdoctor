package com.hss.kgdoctor.domin;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("tb_login_user")
@Data
public class LoginUser {

    private String email;
    private String password;
    private String salt;//加密使用的盐
}
