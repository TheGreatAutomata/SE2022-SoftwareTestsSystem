package com.micro.delegationserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.delegationserver.service.update.UpdateTableService;
import com.micro.dto.DelegationAuditMarketResultDto;
import com.micro.dto.DelegationAuditTestResultDto;
import com.micro.dto.SampleAcceptModelDto;
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
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class auditControllerTest {

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

    @MockBean
    RestTemplate restTemplate;

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
    @Test
    void setRestTemplate() {
        auditController auditController=new auditController();
        RestTemplate restTemplate=new RestTemplate();
        auditController.setRestTemplate(restTemplate);
    }
    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


    @Test
    void auditDelegationByTestEmployees() throws Exception {
        DelegationAuditTestResultDto resultDto=new DelegationAuditTestResultDto();
        resultDto.set确认意见("可以测试");
        String body=toJson(resultDto);
        ResponseEntity<Object> responseEntity=new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.postForEntity(Mockito.anyString(),Mockito.any(HttpEntity.class),Mockito.any()))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/audit/delegation/test/{id}","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
        resultDto.set确认意见("不可以测试");
        body=toJson(resultDto);
        mockMvc.perform(post("/audit/delegation/test/{id}","123")
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
    void auditDelegationByMarketEmployees() throws Exception {
        DelegationAuditMarketResultDto resultDto=new DelegationAuditMarketResultDto();
        resultDto.setResult("可以测试");
        String body=toJson(resultDto);
        ResponseEntity<Object> responseEntity=new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.postForEntity(Mockito.anyString(),Mockito.any(HttpEntity.class),Mockito.any()))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/audit/delegation/market/{id}","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isOk()
                );
        resultDto.setResult("不可以测试");
        body=toJson(resultDto);
        mockMvc.perform(post("/audit/delegation/market/{id}","123")
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