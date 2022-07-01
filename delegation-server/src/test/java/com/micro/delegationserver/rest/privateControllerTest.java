package com.micro.delegationserver.rest;

import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.SampleAndQuantity;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
        delegation.setState(DelegationState.TEST_MARKET_APPLICATION);
        delegation_op = Optional.of(delegation);
        goodDelegationId = "goodDelegationId";
        badDelegationId = "badDelegationId";
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        when(delegationRepository.findById(eq(badDelegationId)))
                .thenReturn(Optional.ofNullable(null));

        when(taskService.createTaskQuery())
                .thenReturn(taskQuery);
        when(runtimeService.createProcessInstanceQuery())
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.processDefinitionKey(Mockito.anyString()))
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.variableValueEquals(Mockito.anyString(),Mockito.any(Object.class)))
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.singleResult())
                .thenReturn(processInstance);
        ArrayList<ProcessInstance> instanceList=new ArrayList<>();
        instanceList.add(processInstance);
        when(processInstanceQuery.list())
                .thenReturn(instanceList);
        when(runtimeService.startProcessInstanceByKey(Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(processInstance);
        when(taskQuery.active())
                .thenReturn(taskQuery);
        when(taskQuery.processDefinitionKey(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.taskName(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.processVariableValueEquals(Mockito.anyString(),Mockito.anyString()))
                .thenReturn(taskQuery);
        ArrayList<Task> list=new ArrayList<>();
        list.add(taskEntity);
        when(taskQuery.list())
                .thenReturn(list);
        when(taskEntity.getId())
                .thenReturn(goodDelegationId);

    }

    private String setDelegationStateUri = "/delegationServer/private/delegationState/{id}/{state}";
    @Test
    void setDelegationStateOk() throws Exception {
        mockMvc.perform(post(setDelegationStateUri, goodDelegationId, DelegationState.TEST_MARKET_CONTRACT).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        Delegation resultDelegation = new Delegation();
        resultDelegation.setState(DelegationState.TEST_MARKET_CONTRACT);
        verify(delegationRepository, times(1)).save(resultDelegation);
    }

    @Test
    void setDelegationStateNotFound() throws Exception {
        mockMvc.perform(post(setDelegationStateUri, badDelegationId, DelegationState.TEST_MARKET_CONTRACT).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }
    private String sampleApplicationFinishedUri = "/delegationServer/private/applicationFinished/{id}";
    @Test
    void sampleApplicationFinishedOk() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        Delegation resultDelegation = new Delegation();
        resultDelegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
        mockMvc.perform(post(sampleApplicationFinishedUri, goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        verify(delegationRepository, times(1)).save(resultDelegation);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegationId", goodDelegationId);
        verify(taskService, times(1)).complete(goodDelegationId, variables);
    }

    @Test
    void sampleApplicationFinishedNotFoundTask() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(null);
        mockMvc.perform(post(sampleApplicationFinishedUri, badDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void sampleApplicationFinishedNotFound() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        mockMvc.perform(post(sampleApplicationFinishedUri, badDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    String changeSampleMethodUri = "/delegationServer/private/changeSampleMethod/{method}/{id}";
    @Test
    void changeSampleMethodNotFound() throws Exception {

        mockMvc.perform(post(changeSampleMethodUri, "online", badDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeSampleMethodFinishedOkOnline() throws Exception {
        Delegation tempDelegation = delegation_op.get();
        Delegation newDelegation = new Delegation();
        newDelegation.setDelegationId(tempDelegation.getDelegationId());
        newDelegation.setState(tempDelegation.getState());
        SampleAndQuantity sampleType = new SampleAndQuantity();
        sampleType.set软件介质("在线上传");
        DelegationApplicationTable applicationTable = new DelegationApplicationTable();
        applicationTable.set样品和数量(sampleType);
        newDelegation.setApplicationTable(applicationTable);
        delegation_op = Optional.of(newDelegation);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        mockMvc.perform(post(changeSampleMethodUri, "online", goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation", newDelegation);
        variables.put("delegationId",goodDelegationId);
        verify(runtimeService, times(1)).startProcessInstanceByKey("delegation_modify",variables);

        newDelegation.getApplicationTable().get样品和数量().set软件介质("硬盘");
        delegation_op = Optional.of(newDelegation);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        mockMvc.perform(post(changeSampleMethodUri, "online", goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        newDelegation.getApplicationTable().get样品和数量().set软件介质("在线上传");
        variables = new HashMap<String, Object>();
        variables.put("delegation", newDelegation);
        variables.put("delegationId",goodDelegationId);
        verify(runtimeService, times(2)).startProcessInstanceByKey("delegation_modify",variables);

        delegation_op = Optional.of(tempDelegation);
    }
    @Test
    void changeSampleMethodFinishedOkOffline() throws Exception {
        Delegation tempDelegation = delegation_op.get();
        Delegation newDelegation = new Delegation();
        newDelegation.setDelegationId(tempDelegation.getDelegationId());
        newDelegation.setState(tempDelegation.getState());
        SampleAndQuantity sampleType = new SampleAndQuantity();
        sampleType.set软件介质("在线上传");
        DelegationApplicationTable applicationTable = new DelegationApplicationTable();
        applicationTable.set样品和数量(sampleType);
        newDelegation.setApplicationTable(applicationTable);
        delegation_op = Optional.of(newDelegation);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        mockMvc.perform(post(changeSampleMethodUri, "offline", goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        Map<String, Object> variables = new HashMap<String, Object>();
        newDelegation.getApplicationTable().get样品和数量().set软件介质("硬盘");
        variables.put("delegation", newDelegation);
        variables.put("delegationId",goodDelegationId);
        verify(runtimeService, times(1)).startProcessInstanceByKey("delegation_modify",variables);

        newDelegation.getApplicationTable().get样品和数量().set软件介质("硬盘");
        delegation_op = Optional.of(newDelegation);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        mockMvc.perform(post(changeSampleMethodUri, "offline", goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        variables = new HashMap<String, Object>();
        variables.put("delegation", newDelegation);
        variables.put("delegationId",goodDelegationId);
        verify(runtimeService, times(2)).startProcessInstanceByKey("delegation_modify",variables);

        delegation_op = Optional.of(tempDelegation);
    }

    private String setContractIdUri = "/delegationServer/private/contractId/{id}/{contractId}";
    @Test
    void setContractIdForNotFound() throws Exception {
        mockMvc.perform(post(setContractIdUri, badDelegationId, "contractId").contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void setContractIdForOk() throws Exception {
        mockMvc.perform(post(setContractIdUri, goodDelegationId, "contractId").contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
        Delegation resultDelegation = new Delegation();
        resultDelegation.setState(DelegationState.TEST_MARKET_APPLICATION);
        resultDelegation.setContractId("contractId");
        verify(delegationRepository, times(1)).save(resultDelegation);
    }
}