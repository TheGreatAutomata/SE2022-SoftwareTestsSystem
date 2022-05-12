package com.micro.delegationserver.service;


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
}