package com.micro.contractserver.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SaveContractFilesDelegateTest {

    @Autowired
    private SaveContractFilesDelegate saveContractFilesDelegate;

    @MockBean
    private DelegateExecution delegateExecution;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void execute() {

        when(delegateExecution.getVariable("contractId"))
                .thenReturn("contractId");
        when(delegateExecution.getVariable("contractId"))
                .thenReturn("contractId");

    }
}