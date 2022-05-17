package com.micro.delegationserver.rest;

import com.micro.api.AuditApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
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

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    //todo:处理没找到id的情况
    @Override
    public ResponseEntity<Void> auditDelegation(String usrName, String usrId, String usrRole, String id, DelegationAuditResultDto delegationAuditResultDto) {
        List<Task> tasks = taskService.createTaskQuery().list();
        Task task=null;
        for (Task t:
             tasks) {
            if(runtimeService.getVariable(t.getExecutionId(),"delegationId",String.class).equals(id)){
                task=t;
                break;
            }
        }
        
        String state=delegationAuditResultDto.getState();
        boolean accepted= state.equals("Accepted");

        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("accepted", accepted);

        Delegation currentDelegation=runtimeService.getVariable(task.getExecutionId(),"delegation",Delegation.class);

        if(accepted){
            currentDelegation.setState(DelegationState.ACCEPTED);
        }else{
            currentDelegation.setState(DelegationState.DENIED);
        }

        taskService.complete(task.getId(), taskVariables);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
