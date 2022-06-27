package com.micro.sampleserver.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class SamplePutServer implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private";

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> variables = delegateExecution.getVariables();
<<<<<<< HEAD
        int isOnline = (int) variables.get("isOnline");
=======
        int isOnline = (int) delegateExecution.getVariable("isOnline");
>>>>>>> 045d88bebda9adae538f7e2de4388f6c1d8291bd
        String uriMethod;
        if(isOnline == 0)
        {
            uriMethod = "offline";
        }
        else
        {
            uriMethod = "online";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String id = (String) delegateExecution.getVariable("id");
        HttpEntity<String> request = new HttpEntity<>("", headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(DELEGATION_URI+"/changeSampleMethod/" + uriMethod + "/" + id, request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }
        delegateExecution.setVariables(variables);
    }
}
