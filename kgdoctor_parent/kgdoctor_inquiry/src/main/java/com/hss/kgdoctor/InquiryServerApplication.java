package com.hss.kgdoctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.hss.kgdoctor.mapper")
public class InquiryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InquiryServerApplication.class,args);
    }
}