package com.micro.delegationserver.service;


import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import com.micro.dto.CreatDelegationRequestDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
public class DelegationService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public void startApplicationProcess() {
        runtimeService.startProcessInstanceByKey("delegationApplication");
    }

    public void storeDelegation()
    {
        System.out.println("Store delegation....\n");
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    public Delegation constructFromRequest(CreatDelegationRequest request){
        return new Delegation(request.getUsrId(),request.getUsrName(),request.getApplicationTable().getName());
    }

    public Delegation constructFromRequestDto(CreatDelegationRequestDto requestDto,String usrId,String usrName){
        Delegation delegation=new Delegation();
        delegation.delegationName=requestDto.getApplicationTable().getName();
        delegation.usrId=usrId;
        delegation.usrName=usrName;
        return delegation;
    }



}