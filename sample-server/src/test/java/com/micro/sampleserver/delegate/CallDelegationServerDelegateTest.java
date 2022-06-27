package com.micro.sampleserver.delegate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CallDelegationServerDelegateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CallDelegationServerDelegate callDelegationServerDelegate;

    @Test
    void setRestTemplate() {
        Assertions.assertThat(restTemplate).isNotNull();
    }

    @Test
    void execute() {

    }
}