package com.hss.kgdoctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.hss.kgdoctor.mapper")
public class KnowledgeSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeSearchApplication.class,args);
    }
}
