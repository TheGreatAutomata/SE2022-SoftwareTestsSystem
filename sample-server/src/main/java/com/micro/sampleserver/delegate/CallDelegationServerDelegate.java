package com.micro.sampleserver.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.dto.StartSampleApplicationRequestDto;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class CallDelegationServerDelegate implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private";
    @Override
    public void execute(DelegateExecution delegateExecution) {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String id = (String) delegateExecution.getVariable("delegationId");
        ResponseEntity<Void> result = restTemplate.postForEntity(DELEGATION_URI+"/applicationFinished/" + id, null, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }
    }
}
