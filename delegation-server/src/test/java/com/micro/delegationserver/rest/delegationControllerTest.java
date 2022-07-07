package com.micro.delegationserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.mapper.DelegationFunctionTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.delegationserver.service.update.UpdateTableService;
import com.micro.delegationserver.service.update.applicationTable.UpdateApplicationTableResult;
import com.micro.delegationserver.service.update.functionTable.UpdateFunctionTableResult;
import com.micro.dto.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.util.BsonUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class delegationControllerTest {

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
    UpdateTableService updateTableService;

    @MockBean
    DelegationRepository delegationRepository;

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

    @Test
    void creatDelegation() throws Exception {
        testDelegation.setDelegationId("testDelegation");
        when(delegationService.constructFromRequestDto(Mockito.any(CreatDelegationRequestDto.class),Mockito.anyString(),Mockito.anyString()))
                .thenReturn(testDelegation);
        String body=toJson(new CreatDelegationRequestDto());
        mockMvc.perform(post("/delegation/applicationTable")
                .header("usrId","")
                .header("usrName","")
                .header("usrRole","")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(
                        status().isCreated()
                );
    }
    @Test
    void updateApplicationTable() throws Exception {
        UpdateApplicationTableResult result=new UpdateApplicationTableResult();
        result.setHttpStatus(HttpStatus.OK);
        when(updateTableService.updateApplicationTable(Mockito.anyString(),Mockito.any(DelegationApplicationTableDto.class)))
                .thenReturn(result);
        String body=toJson(new DelegationApplicationTableDto());
        mockMvc.perform(put("/delegation/{id}/applicationTable","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void updatefileListTable() throws Exception {
        String body=toJson(new DelegationFileListDto());
        mockMvc.perform(put("/delegation/{id}/fileListTable","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void uploadFunctionTable() throws Exception {
        UpdateFunctionTableResult result=new UpdateFunctionTableResult();
        result.setHttpStatus(HttpStatus.OK);
        when(updateTableService.updateFunctionTable(Mockito.anyString(),Mockito.any(DelegationFunctionTableDto.class)))
                .thenReturn(result);
        String body=toJson(new DelegationFunctionTableDto());
        mockMvc.perform(put("/delegation/{id}/functionTable","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
    }
    @Test
    void findDelegation() throws Exception {
        testDelegation.setDelegationId("123");
        testDelegation.setUsrBelonged("testUsr");
        when(delegationRepository.findById("123"))
                .thenReturn(Optional.of(testDelegation));
        DelegationItemDto delegationItemDto=new DelegationItemDto();
        delegationItemDto.setUsrBelonged("testUsr");
        delegationItemDto.setDelegationId("123");
        String body=toJson(new DelegationFileListDto());
        mockMvc.perform(get("/delegation/{id}","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                ).andExpect(
                        content().json(toJson(delegationItemDto))
                );
        ;
    }

    @Test
    void createDelegationFile() {
    }

    @Test
    void listDelegationFile() {
    }

    @Test
    void deleteDelegation() {
    }

    @Test
    void updateFunctionTable() throws Exception {
        UpdateFunctionTableResult result=new UpdateFunctionTableResult();
        result.setHttpStatus(HttpStatus.OK);
        when(updateTableService.updateFunctionTable(Mockito.anyString(),Mockito.any(DelegationFunctionTableDto.class)))
                .thenReturn(result);
        String body=toJson(new DelegationFunctionTableDto());
        mockMvc.perform(put("/delegation/{id}/functionTable","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
    }
}