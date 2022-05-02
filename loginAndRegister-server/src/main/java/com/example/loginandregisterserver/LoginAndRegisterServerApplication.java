package com.example.loginandregisterserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LoginAndRegisterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginAndRegisterServerApplication.class, args);
    }

}
