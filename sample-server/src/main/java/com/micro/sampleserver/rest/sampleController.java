package com.micro.sampleserver.rest;

import com.micro.api.ApiUtil;
import com.micro.api.SampleApi;
import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.service.MinioService;
import com.micro.dto.DelegationItemDto;
import com.micro.dto.GetSampleResponseDto;
import com.micro.dto.SampleAcceptDto;
import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.mapper.SampleMessageMapper;
import com.micro.sampleserver.model.Sample;
import com.micro.sampleserver.repository.MongoDBDelegationRepository;
import io.minio.Result;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
public class sampleController implements SampleApi {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    MongoDBDelegationRepository delegationRepository;
    @Autowired
    private TaskService taskService;

    @Autowired
    MinioService minioServce;

    @Autowired
    SampleMessageMapper sampleMessageMapper;

    @Override
    public ResponseEntity<Void> acceptSample(String usrName, String usrId, String usrRole, String id, SampleAcceptDto sampleAcceptDto) {

    }

    @Override
    public ResponseEntity<Void> changeOnlineSample(String usrName, String usrId, String usrRole, String id, MultipartFile 样品) {
        Task task = taskService.createTaskQuery().taskName("putSampleOrCloseSample").processVariableValueEquals("id",id).singleResult();
        if(task == null)
        {
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("finish", 0);
        variables.put("online", 1);
        variables.put("sampleId", id);
        try {
            variables.put("sample", new MultipartInputStreamFileResource(样品.getBytes(), 样品.getOriginalFilename(), 样品.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(200).build();
    }
    @Override
    public ResponseEntity<Void> createSampleFile(String usrName, String usrId, String usrRole, String id, MultipartFile 样品) {
        Task task = taskService.createTaskQuery().taskName("sampleApplicationOnline").processVariableValueEquals("id",id).singleResult();
        if(task == null)
        {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            variables.put("sample", new MultipartInputStreamFileResource(样品.getBytes(), 样品.getOriginalFilename(), 样品.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        variables.put("sampleId", id);
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> changeOfflineSample(String usrName, String usrId, String usrRole, String id, SampleMessageApplicationRequestDto sampleMessageApplicationRequestDto) {
        Task task = taskService.createTaskQuery().taskName("putSampleOrCloseSample").processVariableValueEquals("id",id).singleResult();
        if(task == null) {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("finish", 0);
        variables.put("online", 0);
        variables.put("sampleId", id);
        variables.put("message", sampleMessageMapper.toObj(sampleMessageApplicationRequestDto));
        variables.put("usrName", usrName);
        variables.put("usrId", usrId);
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> createSampleMessage(String usrName, String usrId, String usrRole, String id, SampleMessageApplicationRequestDto sampleMessageApplicationRequestDto) {
        Task task = taskService.createTaskQuery().taskName("sampleApplicationOffline").processVariableValueEquals("id",id).singleResult();
        if(task == null)
        {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("sampleId", id);
        variables.put("message", sampleMessageMapper.toObj(sampleMessageApplicationRequestDto));
        variables.put("usrName", usrName);
        variables.put("usrId", usrId);
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(201).build();
    }

    @SneakyThrows
    @Override
    public ResponseEntity<GetSampleResponseDto> getSample(String usrName, String usrId, String usrRole, String id) {
        Optional<Sample> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Sample sample=delegation_op.get();
            GetSampleResponseDto getSampleResponseDto = new GetSampleResponseDto();
            getSampleResponseDto.setApplicationMethod("offline");
            getSampleResponseDto.setUri(null);
            getSampleResponseDto.setComment(sample.getComment());
            return new ResponseEntity<GetSampleResponseDto>(getSampleResponseDto,HttpStatus.OK);
        }
        String sampleId = "sample" + id;
        Iterable<Result<Item>> allFiles = minioServce.listObjects(sampleId);
        if(allFiles == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GetSampleResponseDto getSampleResponseDto = new GetSampleResponseDto();
        getSampleResponseDto.setApplicationMethod("online");
        for(Result<Item> f : allFiles)
        {
            getSampleResponseDto.setUri(minioServce.getObjectURL(sampleId, f.get().objectName()));
        }
        getSampleResponseDto.setComment(null);
        return new ResponseEntity<GetSampleResponseDto>(getSampleResponseDto,HttpStatus.OK);
    }

    public Boolean deleteTaskByDelegationId(String id)

    {
        Task task = taskService.createTaskQuery().processDefinitionKey("sample_application").processVariableValueEquals("delegationId",id).singleResult();
        if(task != null){
            runtimeService.deleteProcessInstance(task.getExecutionId(),"sample process has been deleted because delegation is deleted");
            return Boolean.TRUE;
        }
        else return Boolean.FALSE;
    }

    @Override
    public ResponseEntity<Void> deleteOfflineSample(String usrName, String usrId, String usrRole, String id) {

        Optional<Sample> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            delegationRepository.deleteById(delegation_op.get().getDelegationId());
            if(deleteTaskByDelegationId(id) == Boolean.TRUE)  return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Void> deleteOnlineSample(String usrName, String usrId, String usrRole, String id) {

        String sampleId = "sample" + id;
        try {
            minioServce.removeBucket(sampleId);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
