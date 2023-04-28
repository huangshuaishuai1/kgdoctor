package com.hss.kgdoctor.common.domin;

import com.hss.kgdoctor.common.domin.UserEntity;
import lombok.*;

/**
 * Created by hss
 * 用户在登录阶段返回给前端的对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;

    private String userName;

    private Integer role;
}
