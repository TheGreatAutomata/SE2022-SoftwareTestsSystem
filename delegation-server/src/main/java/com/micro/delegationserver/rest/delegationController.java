package com.micro.delegationserver.rest;

import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.micro.api.DelegationApi;
import com.micro.dto.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class delegationController implements DelegationApi{

    @Autowired
    public DelegationService delegationService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public ResponseEntity<String> creatDelegation(String usrName, String usrId, String usrRole, CreatDelegationRequestDto creatDelegationRequestDto) {
        //check
        System.out.println("Number of process instances before: " + runtimeService.createProcessInstanceQuery().count());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("usrName", usrName);
        variables.put("delegationName", usrId);
        //variables.put("vacationMotivation", "I'm really tired!");
        runtimeService.startProcessInstanceByKey("delegationProcess", variables);

        // Verify that we started a new process instance
        //taskService.
        List<Task> tasks = taskService.createTaskQuery().list();
        //taskService.deleteTask();
        //Map<String, Object> tryVars = taskService.getVariables("10007");
        System.out.println("Number of process instances after: " + runtimeService.createProcessInstanceQuery().count());
        return ResponseEntity.ok(usrId);
    }
}
