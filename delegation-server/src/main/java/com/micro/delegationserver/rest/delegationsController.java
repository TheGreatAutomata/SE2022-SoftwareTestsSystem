package com.micro.delegationserver.rest;

import com.micro.api.DelegationApi;
import com.micro.api.DelegationsApi;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.CreatDelegationRequestDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class delegationsController implements DelegationsApi {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public ResponseEntity<List<String>> listDelegations(String usrName, String usrId, String usrRole)
    {
        List<Task> tasks = taskService.createTaskQuery()
                .list();
        //runtimeService.getProcessInstanceEvents()
        //taskService.createTaskQuery().deploymentId()
        List<String> result = null;
        return ResponseEntity.ok(result);
    }
}
