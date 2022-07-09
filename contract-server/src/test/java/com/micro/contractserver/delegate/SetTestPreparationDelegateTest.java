package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractTable;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SetTestPreparationDelegateTest {

    @Autowired
    SetTestPreparationDelegate setTestPreparationDelegate;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<Void> requestEntity;

    private String TEST_URI = "http://test-server/test/prepare/";

    @BeforeEach
    void setUp() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "xxx");
        headers.set("usrName", "xxx");
        headers.set("usrId", "xxx");
        headers.set("usrRole", "xxx");
        requestEntity = new HttpEntity<>(headers);

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
        contract.setProjectId("projectId");

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        when(restTemplate.exchange(TEST_URI + contract.getDelegationId() + "/" + contract.getProjectId(), HttpMethod.PUT, requestEntity, Void.class))
                .thenReturn(ResponseEntity.status(200).build());

        setTestPreparationDelegate.execute(delegateExecution);

        verify(restTemplate, times(1)).exchange(TEST_URI + contract.getDelegationId() + "/" + contract.getProjectId(), HttpMethod.PUT, requestEntity, Void.class);

    }
}