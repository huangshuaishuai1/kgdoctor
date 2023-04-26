package com.hss.kgdoctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by hss
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.hss.kgdoctor.mapper")
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class,args);
    }
}
