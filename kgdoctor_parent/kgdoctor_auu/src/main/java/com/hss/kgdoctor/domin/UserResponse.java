package com.hss.kgdoctor.domin;

import com.hss.kgdoctor.common.domin.UserEntity;
import lombok.*;

/**
 * Created by hss
 * 用户在登录阶段返回给前端的对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String token;
    private UserEntity userInfo;
}
