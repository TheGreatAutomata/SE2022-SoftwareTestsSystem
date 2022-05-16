package com.micro.delegationserver.rest;

import com.micro.delegationserver.mapper.CreatDelegationRequestMapper;
import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.mapper.DelegationFileListMapper;
import com.micro.delegationserver.mapper.DelegationFunctionTableMapper;
import com.micro.delegationserver.model.*;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.micro.api.DelegationApi;
import com.micro.dto.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class delegationController implements DelegationApi{

    @Autowired
    public DelegationService delegationService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    CreatDelegationRequestMapper mapper;


    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    @Autowired
    DelegationFileListMapper delegationFileListMapper;

    @Autowired
    DelegationFunctionTableMapper delegationFunctionTableMapper;

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    @Override
    public ResponseEntity<String> creatDelegation(String usrName, String usrId, String usrRole, CreatDelegationRequestDto creatDelegationRequestDto) {
        Map<String, Object> variables = new HashMap<String, Object>();

        Delegation delegation=delegationService.constructFromRequestDto(creatDelegationRequestDto,usrId,usrName);

        variables.put("delegation",delegation);

        runtimeService.startProcessInstanceByKey("delegation_apply", variables);

        return ResponseEntity.ok(usrId);
    }

    //TODO: 处理异常情况，例如没找到委托
    @Override
    public ResponseEntity<Void> updateApplicationTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {

        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(new HashMap<>());
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation",delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updatefileListTable(String id, DelegationFileListDto delegationFileListDto) {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(new HashMap<>());
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation",delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateFuntionTable(String id, DelegationFunctionTableDto delegationFunctionTableDto) {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(new HashMap<>());
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation",delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DelegationItemDto> findDelegation(String usrName, String usrId, String usrRole, String id) {
        return DelegationApi.super.findDelegation(usrName, usrId, usrRole, id);
    }
}
