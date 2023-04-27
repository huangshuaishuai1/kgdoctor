package com.hss.kgdoctor.feign;

import com.hss.kgdoctor.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("doctor-service")
public interface DoctorFeignClient {
    @GetMapping("/doc/infowithspe/{id}")
    public Result getDoctorInfoWithSpecialtyById(@PathVariable("id") Long id);
}
