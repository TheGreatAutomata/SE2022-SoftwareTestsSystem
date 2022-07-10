package com.micro.sampleserver.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CallDelegationServerDelegateTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CallDelegationServerDelegate callDelegationServerDelegate;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<String> request;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private";
    @BeforeEach
    void init()
    {
        when(delegateExecution.getVariable("id"))
                .thenReturn("sampleId");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>("body", headers);
    }

    @Test
    void execute() {
        when(restTemplate.postForEntity(DELEGATION_URI+"/applicationFinished/sampleId", request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());
        callDelegationServerDelegate.execute(delegateExecution);
        verify(restTemplate,times(1)).postForEntity(DELEGATION_URI+"/applicationFinished/sampleId", request, Void.class);
    }
}