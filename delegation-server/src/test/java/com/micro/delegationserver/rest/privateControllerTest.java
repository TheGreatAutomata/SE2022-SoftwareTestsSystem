package com.micro.delegationserver.rest;

import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class privateControllerTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private DelegationRepository delegationRepository;

    @MockBean
    private DelegationService delegationService;

    @MockBean
    private RuntimeService runtimeService;

    @MockBean
    private TaskQuery taskQuery;

    @MockBean
    private TaskEntity taskEntity;

    @MockBean
    private ProcessInstanceQuery processInstanceQuery;

    @MockBean
    private ProcessInstance processInstance;
    @Autowired
    private MockMvc mockMvc;

    private String goodDelegationId;

    private String badDelegationId;

    Optional<Delegation> delegation_op;

    private HttpHeaders headers;

    @BeforeEach
    void init()
    {
        headers = new HttpHeaders();
        Delegation delegation = new Delegation();
        delegation_op = Optional.of(delegation);
        delegation.setState(DelegationState.TEST_MARKET_APPLICATION);
        goodDelegationId = "goodDelegationId";
        badDelegationId = "badDelegationId";

    }

    private String setDelegationStateUri = "/delegationServer/private/delegationState/{id}/{state}";
    @Test
    void setDelegationStateOk() throws Exception {
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        mockMvc.perform(post(setDelegationStateUri, goodDelegationId, DelegationState.TEST_MARKET_CONTRACT).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        Delegation resultDelegation = new Delegation();
        resultDelegation.setState(DelegationState.TEST_MARKET_CONTRACT);
        verify(delegationRepository, times(1)).save(resultDelegation);
    }

    @Test
    void setDelegationStateNotFound() throws Exception {
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(Optional.of(null));
        mockMvc.perform(post(setDelegationStateUri, goodDelegationId, DelegationState.TEST_MARKET_CONTRACT).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void sampleApplicationFinishedOk() throws Exception {

    }

    @Test
    void sampleApplicationFinishedNotFoundTask() throws Exception {

    }

    @Test
    void sampleApplicationFinishedNotFound() throws Exception {

    }

    @Test
    void changeSampleMethod() throws Exception {
    }

    @Test
    void setContractId() throws Exception {
    }
}