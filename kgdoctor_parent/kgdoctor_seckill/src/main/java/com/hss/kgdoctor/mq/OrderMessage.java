package com.hss.kgdoctor.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hss
 * 封装异步下单的参数
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {
    private Long seckillId;//秒杀商品ID
    private String token;//用户的token信息
    private Long userPhone;//用户手机号码
}
