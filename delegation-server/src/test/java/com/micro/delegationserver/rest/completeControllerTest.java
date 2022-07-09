package com.micro.delegationserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.commonserver.service.MinioService;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.mapper.DelegationFilesMapper;
import com.micro.delegationserver.mapper.DelegationItemMapper;
import com.micro.delegationserver.model.*;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.InlineObjectDto;
import io.minio.Result;
import io.minio.messages.Item;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class completeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DelegationFilesMapper delegationFilesMapper;
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
    private MinioService minioService;

    @MockBean
    DelegationRepository delegationRepository;

    @MockBean
    public DelegationService delegationService;

    Optional<Delegation> delegation_op;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private Result<Item> singleFile;

    @MockBean
    private Item singleItem;

    @Autowired
    private DelegationItemMapper delegationItemMapper;

    private String goodDelegationId;

    private String badDelegationId;

    private HttpHeaders headers;

    private Delegation getInitDelegation()
    {
        Delegation delegation = new Delegation();
        delegation.setState(DelegationState.TEST_MARKET_APPLICATION);
        DelegationFunctionTable delegationFunctionTable = new DelegationFunctionTable();
        delegation.setFunctionTable(delegationFunctionTable);
        OfferTableUnion offerTableUnion = new OfferTableUnion();
        delegation.setOfferTableUnion(offerTableUnion);
        DelegationApplicationTable delegationApplicationTable = new DelegationApplicationTable();
        SampleAndQuantity sampleAndQuantity = new SampleAndQuantity();
        sampleAndQuantity.set软件介质("在线上传");
        delegationApplicationTable.set样品和数量(sampleAndQuantity);
        delegation.setApplicationTable(delegationApplicationTable);
        return delegation;
    }

    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @BeforeEach
    void init()
    {
        goodDelegationId = "goodDelegationId";
        badDelegationId = "badDelegationId";

        Delegation delegation = new Delegation();
        delegation.setState(DelegationState.TEST_MARKET_APPLICATION);
        DelegationFunctionTable delegationFunctionTable = new DelegationFunctionTable();
        delegation.setFunctionTable(delegationFunctionTable);
        OfferTableUnion offerTableUnion = new OfferTableUnion();
        delegation.setOfferTableUnion(offerTableUnion);
        DelegationApplicationTable delegationApplicationTable = new DelegationApplicationTable();
        SampleAndQuantity sampleAndQuantity = new SampleAndQuantity();
        sampleAndQuantity.set软件介质("在线上传");
        delegationApplicationTable.set样品和数量(sampleAndQuantity);
        delegation.setApplicationTable(delegationApplicationTable);

        delegation_op = Optional.of(delegation);
        when(delegationRepository.findById(eq(goodDelegationId)))
                .thenReturn(delegation_op);
        when(delegationRepository.findById(eq(badDelegationId)))
                .thenReturn(Optional.ofNullable(null));

        headers = new HttpHeaders();
        headers.set("usrName", "");
        headers.set("usrId", "");
        headers.set("usrRole", "");
        headers.set("Authorization", "");

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
        when(taskEntity.getExecutionId())
                .thenReturn(goodDelegationId);
        List<Delegation> dList = new ArrayList<>();
        dList.add(delegation);
        when(delegationRepository.findAllByUsrId(Mockito.anyString()))
                .thenReturn(dList);
        when(delegationRepository.findAllByState(DelegationState.valueOf("UPLOAD_SAMPLE")))
                .thenReturn(dList);
        when(delegationRepository.findAll())
                .thenReturn(dList);
    }

    private String completeUri = "/complete/delegation/{id}";

    @Test
    void completeDelegationIdPostNotFound() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(null);
        InlineObjectDto inlineObjectDto = new InlineObjectDto();
        inlineObjectDto.setId(goodDelegationId);
        String body = toJson(inlineObjectDto);
        mockMvc.perform(post(completeUri, goodDelegationId)
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void completeDelegationIdPostOk() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        InlineObjectDto inlineObjectDto = new InlineObjectDto();
        inlineObjectDto.setId(goodDelegationId);
        String body = toJson(inlineObjectDto);
        mockMvc.perform(post(completeUri, goodDelegationId)
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