package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
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
class SetDelegationContractIdDelegateTest {

    @Autowired
    private SetDelegationContractIdDelegate setDelegationContractIdDelegate;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<String> request;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private/contractId/";

    @BeforeEach
    void setUp() {

        when(delegateExecution.getVariable("delegationId"))
                .thenReturn("delegationId");

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
        contract.setContractId("contractId");

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        when(restTemplate.postForEntity(DELEGATION_URI + "delegationId" + "/" + contract.getContractId(), request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());

        setDelegationContractIdDelegate.execute(delegateExecution);

        verify(restTemplate, times(1)).postForEntity(DELEGATION_URI + "delegationId" + "/" + contract.getContractId(), request, Void.class);

    }
}