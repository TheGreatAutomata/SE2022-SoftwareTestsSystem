package com.micro.delegationserver.rest;

import com.micro.delegationserver.mapper.CreatDelegationRequestMapper;
import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.service.DelegationService;
import com.micro.delegationserver.service.MinioServce;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    MinioServce minioServce;

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

        Delegation delegation=new Delegation(usrId,usrName,delegationName);

        variables.put("request",request);
        variables.put("delegation",delegation);

        runtimeService.startProcessInstanceByKey("delegation_apply", variables);

        return ResponseEntity.ok(usrId);
    }
    @Override
    public ResponseEntity<Void> createDelegationFile(String id, String usrName, String usrId, String usrRole, List<MultipartFile> file) {
        for(MultipartFile f : file)
        {

        }
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }
}
