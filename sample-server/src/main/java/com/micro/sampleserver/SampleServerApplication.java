package com.micro.sampleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SampleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleServerApplication.class, args);
    }

}
