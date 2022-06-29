package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.commonserver.model.DelegationState;
import com.micro.contractserver.model.ContractState;
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

        Contract contract = (Contract)delegateExecution.getVariable("contract");

        System.out.println("Set delegation state as " + contract.getContractState()  + " according to contract state...");

        DelegationState delegationState = DelegationState.ERROR;

        switch (contract.getContractState()) {
            case PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM: {
                delegationState = DelegationState.PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM;
                break;
            }
            case PARTYB_UPDATE_PERFORMANCE_TERM: {
                delegationState = DelegationState.PARTYB_UPDATE_PERFORMANCE_TERM;
                break;
            }
            case PARTYA_ACCEPT_PERFORMANCE_TERM: {
                delegationState = DelegationState.PARTYA_ACCEPT_PERFORMANCE_TERM;
                break;
            }
            case PARTYA_REJECT_PERFORMANCE_TERM_FOR_MODIFICATION: {
                delegationState = DelegationState.PARTYA_REJECT_PERFORMANCE_TERM_FOR_MODIFICATION;
                break;
            }
            case PARTYA_REJECT_PERFORMANCE_TERM_TO_END: {
                delegationState = DelegationState.PARTYA_REJECT_PERFORMANCE_TERM_TO_END;
                break;
            }
            case PARTYB_ADD_CONTRACT_TABLE: {
                delegationState = DelegationState.PARTYB_ADD_CONTRACT_TABLE;
                break;
            }
            case PARTYA_ADD_CONTRACT_TABLE: {
                delegationState = DelegationState.PARTYA_ADD_CONTRACT_TABLE;
                break;
            }
            case PARTYB_UPLOAD_SIGNED_CONTRACT: {
                delegationState = DelegationState.PARTYB_UPLOAD_SIGNED_CONTRACT;
                break;
            }
            default: {
                System.out.println("contract state is illegal...");
                break;
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String delegationId = contract.getDelegationId();
        HttpEntity<String> request = new HttpEntity<>("{name:string}", headers);
        ResponseEntity<Void> result = restTemplate.postForEntity(DELEGATION_URI + delegationId + "/" + delegationState.toString(), request, Void.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }

        String performanceTermState = contract.getPerformanceTermState();;

        if(performanceTermState != null) {
            switch (performanceTermState) {
                case "接受": {
                    delegateExecution.setVariable("state", 1);
                    break;
                }
                case "申请再议": {
                    delegateExecution.setVariable("state", 2);
                    break;
                }
                case "不接受": {
                    delegateExecution.setVariable("state", 3);
                    break;
                }
                default: {
                    System.out.println("performance term state is illegal...");
                    break;
                }
            }
        }

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());
        delegateExecution.setVariable("delegationId", contract.getDelegationId());

    }

}
