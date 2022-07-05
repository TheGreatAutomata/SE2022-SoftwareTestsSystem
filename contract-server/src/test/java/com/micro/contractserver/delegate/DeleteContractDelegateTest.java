package com.micro.contractserver.delegate;

import com.micro.contractserver.repository.MongoDBContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@Rollback
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteContractDelegateTest {

    @Autowired
    private DeleteContractDelegate deleteContractDelegate;

    @MockBean
    private DelegateExecution delegateExecution;

    @MockBean
    private MongoDBContractRepository contractRepository;

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

        deleteContractDelegate.execute(delegateExecution);

        verify(contractRepository, times(1)).deleteById("contractId");

    }
}