package com.micro.sampleserver.rest;

import com.micro.api.SampleServerApi;
import com.micro.dto.StartSampleApplicationRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class privateController implements SampleServerApi {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Override
    public ResponseEntity<Void> startSampleApplication(StartSampleApplicationRequestDto startSampleApplicationRequestDto) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("id",startSampleApplicationRequestDto.getId());
        if(Objects.equals(startSampleApplicationRequestDto.getApplicationMethod(), "online"))
        {
            variables.put("state",1);
        }
        else
        {
            variables.put("state",2);
        }

        runtimeService.startProcessInstanceByKey("sample_application", variables);

        return ResponseEntity.status(200).build();
    }
}
