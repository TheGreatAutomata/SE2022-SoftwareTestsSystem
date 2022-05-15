package com.micro.delegationserver.rest;

import com.micro.api.AuditApi;
import com.micro.dto.DelegationAuditResultDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class auditController implements AuditApi {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @Override
    public ResponseEntity<Void> auditDelegation(String usrName, String usrId, String usrRole, String id, DelegationAuditResultDto delegationAuditResultDto) {
        System.out.println("Audit..."+id);
        Task task = taskService.createTaskQuery().taskName("Audit").processVariableValueEquals("applicationId",id).singleResult();
        String state=delegationAuditResultDto.getState();
        boolean accepted= state.equals("Accepted");

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("accepted", accepted);
        taskService.complete(task.getId(), taskVariables);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
