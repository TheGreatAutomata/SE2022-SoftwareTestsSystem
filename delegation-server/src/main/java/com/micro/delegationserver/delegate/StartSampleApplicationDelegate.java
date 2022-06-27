package com.micro.delegationserver.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.dto.StartSampleApplicationRequestDto;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class StartSampleApplicationDelegate implements JavaDelegate {
    @LoadBalanced
    private RestTemplate restTemplate;

    private String SAMPLE_URI = "http://sample-server/sampleServer/private";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        headers.setContentType(MediaType.APPLICATION_JSON);
        StartSampleApplicationRequestDto dto = new StartSampleApplicationRequestDto();
        String sampleMethod = (String) delegateExecution.getVariable("sampleMethod");
        if(Objects.equals(sampleMethod, "在线上传"))
        {
            dto.setApplicationMethod("online");
        }
        else
        {
            dto.setApplicationMethod("offline");
        }
        dto.setId((String) delegateExecution.getVariable("delegationId"));
        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(mapper.writeValueAsString(dto), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Void> result = restTemplate.postForEntity(SAMPLE_URI+"/startApplication", request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }
    }
}
