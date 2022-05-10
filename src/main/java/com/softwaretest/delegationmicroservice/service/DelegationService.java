package com.softwaretest.delegationmicroservice.service;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class DelegationService {

    @Autowired
    RuntimeService runtimeService;


    public DelegationService() throws Exception{
    }

    public void postDelegation(){
        System.out.println("post");
    }


    @Autowired
    ProcessEngine processEngine;
    @Transactional
    public void startProcess(String id){
        runtimeService.startProcessInstanceByKey("delegation");
    }
}
