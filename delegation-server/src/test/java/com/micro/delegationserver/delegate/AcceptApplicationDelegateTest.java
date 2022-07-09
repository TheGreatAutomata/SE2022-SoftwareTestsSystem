package com.micro.delegationserver.delegate;

import io.swagger.annotations.Authorization;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptApplicationDelegateTest {

    @MockBean
    DelegateExecution delegateExecution;

    @MockBean
    RestTemplate restTemplate;

    HttpHeaders headers;

    HttpEntity<String> request;

    String goodId;

    @BeforeEach
    void init()
    {
        goodId = "theId";
        when(delegateExecution.getVariable(Mockito.anyString()))
                .thenReturn(goodId);
        ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.OK);



        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>("body", headers);

        when(restTemplate.postForEntity(eq(closeSampleUri+goodId), eq(request), eq(Void.class)))
                .thenReturn(result);
    }

    @Autowired
    AcceptApplicationDelegate acceptApplicationDelegate;

    private String closeSampleUri = "http://sample-server/sampleServer/private/closeSample/";

    @Test
    void execute() {
        acceptApplicationDelegate.execute(delegateExecution);
        verify(restTemplate, times(1)).postForEntity(eq(closeSampleUri+goodId), eq(request), eq(Void.class));
    }
}