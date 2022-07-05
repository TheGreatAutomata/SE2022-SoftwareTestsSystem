package com.micro.sampleserver.delegate;

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

public class CallDelegationServerDelegate implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private String DELEGATION_URI = "http://delegation-server/delegationServer/private";
    @Override
    public void execute(DelegateExecution delegateExecution) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String id = (String) delegateExecution.getVariable("id");
        HttpEntity<String> request = new HttpEntity<>("body", headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(DELEGATION_URI+"/applicationFinished/" + id, request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK && result.getStatusCode() != HttpStatus.NOT_FOUND)
        {
            throw new RuntimeException();
        }
    }
}
