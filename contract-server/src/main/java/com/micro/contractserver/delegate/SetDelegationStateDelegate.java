package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SetDelegationStateDelegate implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private/delegationState/";

    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("Set delegation state according to contract state...");

        Contract contract = (Contract)delegateExecution.getVariable("contract");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String delegationId = (String) delegateExecution.getVariable("delegationId");
        HttpEntity<String> request = new HttpEntity<>("{name:string}", headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(DELEGATION_URI + delegationId + "/" + contract.getContractId(), request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());
        delegateExecution.setVariable("delegationId", delegationId);

    }

}
