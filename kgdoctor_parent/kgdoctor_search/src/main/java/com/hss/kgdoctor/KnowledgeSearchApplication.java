package com.hss.kgdoctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@MapperScan(basePackages = "com.hss.kgdoctor.mapper")
public class KnowledgeSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeSearchApplication.class,args);
    }
}
