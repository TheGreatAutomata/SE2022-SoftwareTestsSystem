package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractState;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SetDelegationStateDelegateTest {

    @Autowired
    SetDelegationStateDelegate setDelegationStateDelegate;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<String> request;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private/delegationState/";

    @BeforeEach
    void setUp() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>("{name:string}", headers);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setRestTemplate() {
    }

    @Test
    void execute() {

        Contract contract = new Contract("delegationId", null);
        contract.setContractState(ContractState.PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM);

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        when(restTemplate.postForEntity(DELEGATION_URI + "delegationId" + "/" + ContractState.PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM.toString(), request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());

        setDelegationStateDelegate.execute(delegateExecution);

        verify(restTemplate, times(1)).postForEntity(DELEGATION_URI + "delegationId" + "/" + ContractState.PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM.toString(), request, Void.class);

    }
}