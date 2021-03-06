package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.repository.DelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AcceptApplicationDelegate implements JavaDelegate {
    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String closeSampleUri = "http://sample-server/sampleServer/private/closeSample/";

    /**
     * 结束委托流程
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
//        System.out.println("Save the delegation.");
//        Delegation delegation=(Delegation) delegateExecution.getVariable("delegation");
//        delegation.setState(DelegationState.QUOTATION_MARKET);
//        delegationRepository.save(delegation);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String id = (String) delegateExecution.getVariable("delegationId");
        HttpEntity<String> request = new HttpEntity<>("body", headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(closeSampleUri+id, request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }
    }
}
