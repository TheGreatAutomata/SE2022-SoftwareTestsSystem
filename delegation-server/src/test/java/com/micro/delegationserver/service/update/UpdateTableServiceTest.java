package com.micro.delegationserver.service.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.delegate.UpdateApplicationDelegate;
import com.micro.delegationserver.delegate.UpdateDelegationDelegate;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.delegationserver.service.update.applicationTable.*;
import com.micro.delegationserver.service.update.functionTable.*;
import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFunctionTableDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UpdateTableServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskQuery taskQuery;
    @MockBean
    private TaskEntity taskEntity;
    @MockBean
    private RuntimeService runtimeService;
    @MockBean
    private ProcessInstanceQuery processInstanceQuery;
    @MockBean
    private ProcessInstance processInstance;

    @MockBean
    DelegationService delegationService;

    @MockBean
    DelegationRepository delegationRepository;

    @MockBean
    UpdateApplicationDelegate updateApplicationDelegate;

    @MockBean
    UpdateDelegationDelegate updateDelegationDelegate;

    Delegation testDelegation;

    @BeforeEach
    void init(){
        testDelegation=new Delegation();
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

        when(taskQuery.active())
                .thenReturn(taskQuery);
        when(taskQuery.processDefinitionKey(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.taskName(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.processVariableValueEquals(Mockito.anyString(),Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        ArrayList<Task> list=new ArrayList<>();
        list.add(taskEntity);
        when(taskQuery.list())
                .thenReturn(list);
        when(taskEntity.getExecutionId())
                .thenReturn("123");
        when(delegationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(testDelegation));
        when(delegationRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(Optional.of(testDelegation));
    }
    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Autowired
    UpdateApplicationTableDelegation_UPLOAD_FUNCTION_TABLE function_table;

    @Autowired
    UpdateApplicationTableDelegation_AFTER_APPLY after_apply;

    @Autowired
    UpdateFunctionTableDelegation_UPLOAD_FILES upload_files;

    @Autowired
    UpdateFunctionTableDelegation_AFTER_APPLY func_after_apply;

    @Autowired
    UpdateApplicationTable_CANNOT_MODIFY c0;

    @Autowired
    UpdateFunctionTable_CANNOT_MODIFY c1;

    @Autowired
    UpdateTableService updateTableService;

    @Test
    void dispatchApplication() {
        UpdateApplicationTableDelegation delegate= updateTableService.dispatchApplication(DelegationState.TEST_MARKET_CONTRACT);
        assertEquals(delegate.getClass(), UpdateApplicationTableDelegation_AFTER_APPLY.class);
    }

    @Test
    void dispatchFunction() {
        UpdateFunctionTableDelegation delegate= updateTableService.dispatchFunction(DelegationState.TEST_MARKET_CONTRACT);
        assertEquals(delegate.getClass(), UpdateFunctionTableDelegation_AFTER_APPLY.class);
    }

    @Test
    void updateApplicationTable() {
        testDelegation.setState(DelegationState.TEST_MARKET_CONTRACT);
        when(delegationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(testDelegation));
        updateTableService.updateApplicationTable("123",new DelegationApplicationTableDto());
        verify(delegationRepository,times(2)).findById(Mockito.anyString());
    }

    @Test
    void updateFunctionTable() {
        testDelegation.setState(DelegationState.TEST_MARKET_CONTRACT);
        when(delegationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(testDelegation));
        updateTableService.updateFunctionTable("123",new DelegationFunctionTableDto());
        verify(delegationRepository,times(2)).findById(Mockito.anyString());
    }

    @Test
    void TestHandlers(){
        UpdateApplicationTableResult result=function_table.updateTable("123",new DelegationApplicationTableDto());
        assertEquals(result.getHttpStatus(), HttpStatus.OK);
        result=after_apply.updateTable("123",new DelegationApplicationTableDto());
        assertEquals(result.getHttpStatus(), HttpStatus.OK);
        result=c0.updateTable("123",new DelegationApplicationTableDto());
        assertEquals(result.getHttpStatus(), HttpStatus.FORBIDDEN);

        UpdateFunctionTableResult result2=upload_files.updateTable("123",new DelegationFunctionTableDto());
        assertEquals(result2.getHttpStatus(),HttpStatus.OK);
        result2=func_after_apply.updateTable("123",new DelegationFunctionTableDto());
        assertEquals(result2.getHttpStatus(),HttpStatus.OK);
        result2=c1.updateTable("123",new DelegationFunctionTableDto());
        assertEquals(result2.getHttpStatus(),HttpStatus.FORBIDDEN);

    }
}