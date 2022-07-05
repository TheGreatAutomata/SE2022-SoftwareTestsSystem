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
public class TestServerApplication implements CommandLineRunner{
    public static void main(String args[]){
        SpringApplication.run(TestServerApplication.class,args);
    }

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Override
    public void run(String... args) throws Exception {
        String id = "62c42f9d6d7d8d0908e26a03";
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId", id).list();
        System.out.println(list);
        System.out.println(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId", id).singleResult()!=null);
        List<Task> tasks = taskService.createTaskQuery().taskName("UploadWorkEvaluationTable").processDefinitionKey("test_reaudit").processVariableValueEquals("delegationId", id).list();
        System.out.println(tasks);
    }
}
