/*
package com.micro.delegationserver.delegate;

import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.delegationserver.model.OfferTableUnion;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SaveCompleteTest {

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    DelegateExecution delegateExecution;

    @Autowired
    SaveComplete saveComplete;

    private Delegation getInitDelegation()
    {
        Delegation delegation = new Delegation();
        delegation.setState(DelegationState.QUOTATION_USER_APPLICATION);
        DelegationFunctionTable delegationFunctionTable = new DelegationFunctionTable();
        delegation.setFunctionTable(delegationFunctionTable);
        OfferTableUnion offerTableUnion = new OfferTableUnion();
        delegation.setOfferTableUnion(offerTableUnion);
        return delegation;
    }

    @BeforeEach
    void init()
    {
        when(delegateExecution.getVariable(Mockito.anyString()))
                .thenReturn(getInitDelegation());
    }

    @Test
    void execute() {
        saveComplete.execute(delegateExecution);
        verify(mongoTemplate, times(1)).save(Mockito.anyString(), Mockito.anyString());
    }
}*/
