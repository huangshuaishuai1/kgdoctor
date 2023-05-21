package com.hss.kgdoctor.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    // 用户token
    private String token;
    // 挂号Id
    private Integer registerId;

}
