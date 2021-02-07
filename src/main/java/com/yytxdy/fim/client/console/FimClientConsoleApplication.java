package com.yytxdy.fim.client.console;

import com.yytxdy.fim.client.console.netty.ConsoleClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FimClientConsoleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FimClientConsoleApplication.class, args);
        ConsoleClient nettyServer = context.getBean(ConsoleClient.class);
        nettyServer.startClient();
    }
}
