package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SetTestPreparationDelegate implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String TEST_URI = "http://test-server/test/prepare/";

    @Override
    public void execute(DelegateExecution delegateExecution) {

        // System.out.println("...Setting preparation for the test");

        Contract contract = (Contract)delegateExecution.getVariable("contract");

        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "xxx");
        headers.set("usrName", "xxx");
        headers.set("usrId", "xxx");
        headers.set("usrRole", "xxx");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> result = restTemplate.exchange(TEST_URI + contract.getDelegationId() + "/" + contract.getProjectId(), HttpMethod.PUT, requestEntity, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            // System.out.println("!!! set test preparation failed !!!");
            throw new RuntimeException();
        }

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());
        delegateExecution.setVariable("delegationId", contract.getDelegationId());

    }

}
