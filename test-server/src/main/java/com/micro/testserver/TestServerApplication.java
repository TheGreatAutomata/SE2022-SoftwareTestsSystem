package com.micro.testserver;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
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
public class TestServerApplication{
    public static void main(String args[]){
        SpringApplication.run(TestServerApplication.class,args);
    }

    @Autowired
    RuntimeService runtimeService;

    /*@Override
    public void run(String... args) throws Exception {
        String id="62c420566d7d8d0908e26a00";
        List<ProcessInstance> list= runtimeService.createProcessInstanceQuery().processDefinitionKey("test_reaudit").variableValueEquals("delegationId",id).list();
        System.out.println(list
                .size());
        System.out.println(list);
        for (ProcessInstance instance:list
             ) {
            runtimeService.deleteProcessInstance(instance.getId(),"你气数已尽");
        }

    }*/
}
