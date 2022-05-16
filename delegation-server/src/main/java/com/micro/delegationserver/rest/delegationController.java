package com.micro.delegationserver.rest;

import com.google.common.collect.Lists;
import com.micro.delegationserver.mapper.CreatDelegationRequestMapper;
import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.mapper.DelegationFileListMapper;
import com.micro.delegationserver.mapper.DelegationFunctionTableMapper;
import com.micro.delegationserver.model.*;
import com.micro.delegationserver.model.dao.CreatDelegationRequestDao;
import com.micro.delegationserver.service.DelegationService;
import com.micro.delegationserver.service.MinioServce;
import io.minio.messages.Bucket;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
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
    CreatDelegationRequestMapper mapper;

    @Autowired
    CreatDelegationRequestDao creatDelegationRequestDao;

    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    @Autowired
    DelegationFileListMapper delegationFileListMapper;

    @Autowired
    DelegationFunctionTableMapper delegationFunctionTableMapper;

    @Override
    public ResponseEntity<String> creatDelegation(String usrName, String usrId, String usrRole, CreatDelegationRequestDto creatDelegationRequestDto) {
        //check
        /*System.out.println("Number of process instances before: " + runtimeService.createProcessInstanceQuery().count());
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
        System.out.println("Number of process instances after: " + runtimeService.createProcessInstanceQuery().count());*/

        Map<String, Object> variables = new HashMap<String, Object>();

        String delegationName=creatDelegationRequestDto.getApplicationTable().getName();
        String functionName=creatDelegationRequestDto.getFunctionTable().getName();

        CreatDelegationRequest request=mapper.toCreatDelegationRequest(creatDelegationRequestDto);

        request.setUsrId(usrId);
        request.setUsrName(usrName);

        Delegation delegation = new Delegation(usrId,usrName,delegationName);

        variables.put("request",request);
        variables.put("delegation",delegation);

        runtimeService.startProcessInstanceByKey("delegation_apply", variables);

        return ResponseEntity.ok(usrId);
    }

    //TODO: 处理异常情况，例如没找到委托
    @Override
    public ResponseEntity<Void> updateApplicationTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {

        CreatDelegationRequest request=creatDelegationRequestDao.Get(Long.valueOf(id));
        DelegationApplicationTable applicationTable=delegationApplicationTableMapper.toDelegationApplicationTable(delegationApplicationTableDto);
        request.setApplicationTable(applicationTable);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("request",request);
        Delegation delegation=delegationService.constructFromRequest(request);
        variables.put("delegation",delegation);
        variables.put("applicationId",id);
        runtimeService.startProcessInstanceByKey("delegation_modify", variables);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updatefileListTable(String id, DelegationFileListDto delegationFileListDto) {
        CreatDelegationRequest request=creatDelegationRequestDao.Get(Long.valueOf(id));
        DelegationFileList fileList=delegationFileListMapper.toObj(delegationFileListDto);
        request.setFileList(fileList);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("request",request);
        Delegation delegation=delegationService.constructFromRequest(request);
        variables.put("delegation",delegation);
        variables.put("applicationId",id);
        runtimeService.startProcessInstanceByKey("delegation_modify", variables);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateFuntionTable(String id, DelegationFunctionTableDto delegationFunctionTableDto) {
        CreatDelegationRequest request = creatDelegationRequestDao.Get(Long.valueOf(id));
        DelegationFunctionTable functionTable = delegationFunctionTableMapper.toObj(delegationFunctionTableDto);
        request.setFunctionTable(functionTable);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("request", request);
        Delegation delegation = delegationService.constructFromRequest(request);
        variables.put("delegation", delegation);
        variables.put("applicationId", id);
        runtimeService.startProcessInstanceByKey("delegation_modify", variables);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Void> createDelegationFile(String id, String usrName, String usrId, String usrRole, MultipartFile file1, MultipartFile file2, MultipartFile file3, MultipartFile file4) {
        //usrName暂时作bucketName
        //Optional<Bucket> delegationBucket = minioServce.getBucket(usrName);
        Task task = taskService.createTaskQuery().taskName("FilesUpload").processVariableValueEquals("applicationId",id).singleResult();

        Map<String, Object> variables = new HashMap<String, Object>();
        String delegationId = "delega"+id;
        variables.put("delegationId", delegationId);
        //List<MultipartFile> filesList = Lists.newArrayList(file1, file2, file3, file4);
        variables.put("file1", file1.getBytes());
        //variables.put("file2", file2);
        //variables.put("file3", file3);
        //variables.put("file4", file4);
        taskService.complete(task.getId(), variables);

//        if(delegationService.creatFile(id, "file1", file1) && delegationService.creatFile(id, "file2", file2) && delegationService.creatFile(id, "file3", file3) && delegationService.creatFile(id, "file4", file4))
//        {
        return ResponseEntity.status(200).build();
//        }

    }
}

