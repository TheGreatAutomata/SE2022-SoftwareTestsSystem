package com.micro.delegationserver.rest;


import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.mapper.DelegationFileListMapper;
import com.micro.delegationserver.mapper.DelegationFunctionTableMapper;

import com.google.common.collect.Lists;
import com.micro.delegationserver.mapper.*;

import com.micro.delegationserver.model.*;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.delegationserver.service.DelegationService;

import com.sun.mail.imap.protocol.ID;

import com.micro.delegationserver.service.MinioServce;
import io.minio.messages.Bucket;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.SneakyThrows;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import org.activiti.engine.task.Task;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.micro.api.DelegationApi;
import com.micro.dto.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
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
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    @Autowired
    DelegationFileListMapper delegationFileListMapper;

    @Autowired
    DelegationFunctionTableMapper delegationFunctionTableMapper;

    @Autowired
    MongoDBDelegationRepository delegationRepository;
    @Autowired
    DelegationFilesMapper delegationFilesMapper;


    @Override
    public ResponseEntity<String> creatDelegation(String usrName, String usrId, String usrRole, CreatDelegationRequestDto creatDelegationRequestDto) {
        Map<String, Object> variables = new HashMap<String, Object>();

        Delegation delegation=delegationService.constructFromRequestDto(creatDelegationRequestDto,usrId,usrName);

        delegation.setState(DelegationState.UPLOAD_FUNCTION_TABLE);

        variables.put("delegation",delegation);

        runtimeService.startProcessInstanceByKey("delegation_apply", variables);

        return ResponseEntity.ok(usrId);
    }


    @Autowired
    MongoTemplate mongoTemplate;

    //TODO: 处理异常情况，例如没找到委托
    @Override
    public ResponseEntity<Void> updateApplicationTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {
        System.out.println("Updating...");

        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(delegationApplicationTableMapper.toDelegationApplicationTable(delegationApplicationTableDto));
            //delegationRepository.updateDelegation(delegation);
            delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation", delegation);
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
            Map<String, Object> variables = new HashMap<String, Object>();
            delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
            variables.put("delegation",delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> uploadFunctionTable(String id, String usrName, String usrId, String usrRole, DelegationFunctionTableDto delegationFunctionTableDto) {
        Map<String, Object> variables = new HashMap<String, Object>();

        Task task=taskService.createTaskQuery().taskName("FunctionTableUpload").processVariableValueEquals("delegationId",id).singleResult();

        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setState(DelegationState.UPLOAD_FILES);
            delegation.setFunctionTable(delegationFunctionTableMapper.toObj(delegationFunctionTableDto));
            runtimeService.setVariable(task.getExecutionId(),"delegation",delegation);
            taskService.complete(task.getId());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DelegationItemDto> findDelegation(String usrName, String usrId, String usrRole, String id) {
        DelegationItemDto delegationItemDto=new DelegationItemDto();
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegationItemDto.setDelegationId(id);
            delegationItemDto.setApplicationTable(delegationApplicationTableMapper.toDelegationApplicationTableDto(delegation.getApplicationTable()));
            delegationItemDto.setState(delegation.getState().toString());
            delegationItemDto.setFileList(null);
            delegationItemDto.setUsrBelonged(delegation.getUsrBelonged());
            return new ResponseEntity<DelegationItemDto>(delegationItemDto,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Void> createDelegationFile(String id, String usrName, String usrId, String usrRole, MultipartFile usrManual, MultipartFile installationManual, MultipartFile operationManual, MultipartFile maintenanceManual) {
        //usrName暂时作bucketName
        //Optional<Bucket> delegationBucket = minioServce.getBucket(usrName);

        //start test
        //here check

        Task task = taskService.createTaskQuery().taskName("FilesUpload").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            //application not found
            ResponseEntity.status(404).build();
        }
        //end test

        Map<String, Object> variables = new HashMap<String, Object>();
        //String delegationId = "delega"+id;
        variables.put("delegationId", id);
        //List<MultipartFile> filesList = Lists.newArrayList(file1, file2, file3, file4);

        if(usrManual != null)
        {
            variables.put("usrManual", usrManual.getBytes());
            variables.put("usrManualName", usrManual.getOriginalFilename());
        }
        else
        {
            variables.put("usrManual", usrManual);
            variables.put("usrManualName", "None");
        }
        if(installationManual != null)
        {
            variables.put("installationManual", installationManual.getBytes());
            variables.put("installationManualName", installationManual.getOriginalFilename());
        }
        else
        {
            variables.put("installationManual", installationManual);
            variables.put("installationManualName", "None");
        }
        if(operationManual != null)
        {
            variables.put("operationManual", operationManual.getBytes());
            variables.put("operationManualName", operationManual.getOriginalFilename());
        }
        else
        {
            variables.put("operationManual", operationManual);
            variables.put("operationManualName", "None");
        }
        if(maintenanceManual != null)
        {
            variables.put("maintenanceManual", maintenanceManual.getBytes());
            variables.put("maintenanceManualName", maintenanceManual.getOriginalFilename());
        }
        else
        {
            variables.put("maintenanceManual", maintenanceManual);
            variables.put("maintenanceManualName", "None");
        }
        //variables.put("installationManual", installationManual.getBytes());
        //variables.put("operationManual", operationManual.getBytes());
        //variables.put("maintenanceManual", maintenanceManual.getBytes());
        //variables.put("file2", file2);
        //variables.put("file3", file3);
        //variables.put("file4", file4);

        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
            delegationRepository.save(delegation);
        }

        taskService.complete(task.getId(), variables);

//        if(delegationService.creatFile(id, "file1", file1) && delegationService.creatFile(id, "file2", file2) && delegationService.creatFile(id, "file3", file3) && delegationService.creatFile(id, "file4", file4))
//        {
        return ResponseEntity.status(201).build();
//        }
    }



    @Override
    public ResponseEntity<AllFilesDto> listDelegationFile(String id, String usrName, String usrId, String usrRole)
    {
        String delegationId = "delega" + id;
        List<minioFileItem> mp = delegationService.getAllFiles(delegationId);
        if(mp == null)
        {
            return ResponseEntity.status(404).build();
        }
        else
        {
            return ResponseEntity.ok(delegationFilesMapper.toAllFilesDto(mp));
        }
    }

    @Override
    public ResponseEntity<Void> deleteDelegation(String usrName, String usrId, String usrRole, String id) {
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            delegationRepository.deleteById(delegation_op.get().getDelegationId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> updateFunctionTable(String id, String usrName, String usrId, String usrRole, DelegationFunctionTableDto delegationFunctionTableDto) {
        System.out.println("Updating...");

        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setFunctionTable(delegationFunctionTableMapper.toObj(delegationFunctionTableDto));
            //delegationRepository.updateDelegation(delegation);
            delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation", delegation);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

