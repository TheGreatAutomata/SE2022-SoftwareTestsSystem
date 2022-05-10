package com.softwaretest.delegationmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication
public class DelegationMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelegationMicroServiceApplication.class, args);
    }

}
