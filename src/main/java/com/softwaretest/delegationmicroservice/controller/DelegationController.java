package com.softwaretest.delegationmicroservice.controller;

import com.softwaretest.delegationmicroservice.service.DelegationService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelegationController {

    @Autowired
    DelegationService delegationService;

    @GetMapping("/submit-delegation/{Id}")
    public String submitDelegation(@PathVariable("Id") String Id){
        delegationService.startProcess(Id);
        return "OK";
    }
}
