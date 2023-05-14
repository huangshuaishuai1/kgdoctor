package com.hss.spring;

import com.hss.spring.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSocketApplication implements CommandLineRunner {
    @Autowired
    private WebSocketServer webSocketServer;

    public static void main(String[] args) throws InterruptedException{
        SpringApplication.run(WebSocketApplication.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        webSocketServer.run();
    }
}
