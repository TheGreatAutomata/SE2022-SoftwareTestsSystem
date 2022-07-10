package com.micro.delegationserver.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StartSampleApplicationDelegateTest {

    @MockBean
    RestTemplate restTemplate;

    private String SAMPLE_URI = "http://sample-server/sampleServer/private";

    @MockBean
    DelegateExecution delegateExecution;

    @Autowired
    StartSampleApplicationDelegate startSampleApplicationDelegate;

    @Test
    void execute() {
        when(delegateExecution.getVariable(Mockito.anyString()))
                .thenReturn("在线上传");
        ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.OK);


        when(restTemplate.postForEntity(eq(SAMPLE_URI+"/startApplication"), Mockito.any(), eq(Void.class)))
                .thenReturn(result);
        startSampleApplicationDelegate.execute(delegateExecution);
        verify(restTemplate, times(1)).postForEntity(eq(SAMPLE_URI+"/startApplication"), Mockito.any(), eq(Void.class));
    }
}