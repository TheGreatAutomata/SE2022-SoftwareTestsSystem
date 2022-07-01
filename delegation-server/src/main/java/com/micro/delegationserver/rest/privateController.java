package com.micro.delegationserver.rest;

import com.micro.api.DelegationServerApi;
import com.micro.delegationserver.model.Delegation;

import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.repository.DelegationRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class privateController implements DelegationServerApi {

    @Autowired
    private TaskService taskService;

    @Autowired
    DelegationRepository delegationRepository;

//    @Autowired
//    MongoTemplate mongoTemplate;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public ResponseEntity<Void> setDelegationState(String id, String state)
    {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setState(DelegationState.valueOf(state));
            delegationRepository.save(delegation);
            return ResponseEntity.status(200).build();
        }else
        {
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<Void> sampleApplicationFinished(String id) {
        Task task = taskService.createTaskQuery().taskName("SampleApplicationFinished").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegationId", id);
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
            delegationRepository.save(delegation);
        }
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> changeSampleMethod(String id, String method) {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            String preMethod = delegation.getApplicationTable().get样品和数量().get软件介质();
            if(Objects.equals(method, "online") && !Objects.equals(preMethod, "在线上传"))
            {
                delegation.getApplicationTable().get样品和数量().set软件介质("在线上传");
            }else if(!Objects.equals(method, "online") && Objects.equals(preMethod, "在线上传"))
            {
                delegation.getApplicationTable().get样品和数量().set软件介质("硬盘");
            }
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation", delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
            return ResponseEntity.status(200).build();
        }else
        {
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<Void> setContractId(String id, String contractId)
    {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setContractId(contractId);
            delegationRepository.save(delegation);
            return ResponseEntity.status(200).build();
        }else
        {
            return ResponseEntity.status(404).build();
        }
    }
}
