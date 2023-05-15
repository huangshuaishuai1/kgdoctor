package com.hss.kgdoctor;

import com.hss.kgdoctor.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class WebSocketApplication implements CommandLineRunner {
    @Autowired
    private WebSocketServer webSocketServer;

    public static void main(String[] args) throws InterruptedException{
        SpringApplication.run(WebSocketApplication.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    webSocketServer.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
