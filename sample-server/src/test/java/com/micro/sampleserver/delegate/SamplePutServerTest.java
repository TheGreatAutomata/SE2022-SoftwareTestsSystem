package com.micro.sampleserver.delegate;

import org.activiti.engine.delegate.DelegateExecution;
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
class SamplePutServerTest {

    @MockBean
    private RestTemplate restTemplate;

    private String DELEGATION_URI = "http://delegation-server/delegationServer/private";

    @Autowired
    private SamplePutServer samplePutServer;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<String> request;

    @BeforeEach
    void init()
    {
        when(delegateExecution.getVariable("id"))
                .thenReturn("sampleId");
        when(delegateExecution.getVariables())
                .thenReturn(null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>("", headers);
    }

    @Test
    void executeOffline() {
        when(delegateExecution.getVariable("isOnline"))
                .thenReturn(0);
        when(restTemplate.postForEntity(DELEGATION_URI+"/applicationFinished/sampleId", request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());
        String uriMethod = "offline";
        String id = "sampleId";
        String uri = DELEGATION_URI+"/changeSampleMethod/" + uriMethod + "/" + id;
        when(restTemplate.postForEntity(uri, request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());
        samplePutServer.execute(delegateExecution);
        verify(restTemplate,times(1)).postForEntity(DELEGATION_URI+"/changeSampleMethod/" + uriMethod + "/" + id, request, Void.class);
    }

    @Test
    void executeOnline() {
        when(delegateExecution.getVariable("isOnline"))
                .thenReturn(1);
        String uriMethod = "online";
        String id = "sampleId";
        when(restTemplate.postForEntity(DELEGATION_URI+"/changeSampleMethod/" + uriMethod + "/" + id, request, Void.class))
                .thenReturn(ResponseEntity.status(200).build());
        samplePutServer.execute(delegateExecution);
        verify(restTemplate,times(1)).postForEntity(DELEGATION_URI+"/changeSampleMethod/" + uriMethod + "/" + id, request, Void.class);
    }
}