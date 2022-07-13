package com.micro.testserver;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration
public class TestServerApplication {
    public static void main(String args[]){
        SpringApplication.run(TestServerApplication.class,args);
    }

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

}
