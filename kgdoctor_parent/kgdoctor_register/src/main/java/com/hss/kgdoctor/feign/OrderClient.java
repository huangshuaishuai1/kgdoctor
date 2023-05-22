package com.hss.kgdoctor.feign;

import com.hss.kgdoctor.common.web.Resp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("order-service")
public interface OrderClient {
    @PostMapping("/register/{userId}/{registerId}")
    Resp createOrder(@PathVariable("userId") Integer userId, @PathVariable("registerId") Integer registerId) ;

}
