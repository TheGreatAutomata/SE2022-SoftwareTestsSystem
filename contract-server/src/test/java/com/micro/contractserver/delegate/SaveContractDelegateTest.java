package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.repository.MongoDBContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class SaveContractDelegateTest {

    @Autowired
    private SaveContractDelegate saveContractDelegate;

    @MockBean
    private DelegateExecution delegateExecution;

    @MockBean
    private MongoDBContractRepository contractRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

        contractRepository.deleteById("contractId");

    }

    @Test
    void execute() {

        Contract contract = new Contract();

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        saveContractDelegate.execute(delegateExecution);

        verify(contractRepository, times(1)).save(contract);

    }
}