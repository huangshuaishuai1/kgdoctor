package com.hss.kgdoctor.feign;

import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("doctor-service")
public interface DoctorFeignClient {
    @GetMapping("/doc/exist/{id}")
    public Boolean isExist(@PathVariable("id") Long id);

    @GetMapping("/doc/info/{id}")
    public Result<DoctorEntity> getDoctorInfoById(@PathVariable("id") Long id);


    }