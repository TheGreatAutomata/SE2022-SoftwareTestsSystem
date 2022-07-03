package com.micro.testserver;

import com.micro.contractserver.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration
public class TestServerApplication{
    public static void main(String args[]){
        SpringApplication.run(TestServerApplication.class,args);
    }
}
