package com.micro.delegationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DelegationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelegationServerApplication.class, args);
    }

}
