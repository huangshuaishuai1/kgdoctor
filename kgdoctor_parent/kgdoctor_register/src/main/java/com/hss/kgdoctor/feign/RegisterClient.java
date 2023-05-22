package com.hss.kgdoctor.feign;

import com.hss.kgdoctor.common.web.Resp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("product-service")
public interface RegisterClient {
    @PostMapping("/stock/regster/dec/{id}")
    Resp decrStock(@PathVariable Integer registerId) ;

}
