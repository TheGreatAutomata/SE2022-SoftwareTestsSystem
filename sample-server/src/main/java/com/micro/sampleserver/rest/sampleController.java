package com.micro.sampleserver.rest;

import com.micro.api.ApiUtil;
import com.micro.api.SampleApi;
import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.service.MinioService;
import com.micro.dto.GetSampleResponseDto;
import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.mapper.SampleMessageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class sampleController implements SampleApi {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    MinioService minioServce;

    @Autowired
    SampleMessageMapper sampleMessageMapper;

    @Override
    public ResponseEntity<Void> createSampleFile(String usrName, String usrId, String usrRole, String id, MultipartFile sample) {
        Task task = taskService.createTaskQuery().taskName("sampleApplicationOnline").processVariableValueEquals("id",id).singleResult();
        if(task == null)
        {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            variables.put("sample", new MultipartInputStreamFileResource(sample.getBytes(), sample.getOriginalFilename(), sample.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        variables.put("sampleId", id);
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(201).build();
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

    @Override
    public ResponseEntity<GetSampleResponseDto> getSample(String usrName, String usrId, String usrRole, String id) {
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }
}
