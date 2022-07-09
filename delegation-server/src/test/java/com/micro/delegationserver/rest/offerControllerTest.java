package com.micro.delegationserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.commonserver.service.MinioService;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.mapper.OfferConfirmationMapper;
import com.micro.delegationserver.mapper.OfferTableMapper;
import com.micro.delegationserver.mapper.ProjectOfferItemMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.delegationserver.model.OfferTableUnion;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.OfferReplyRequestDto;
import com.micro.dto.OfferRequestDto;
import com.micro.dto.OfferSignItemDto;
import com.micro.dto.OfferTableDto;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class offerControllerTest {

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
    private MinioService minioService;

    @MockBean
    DelegationRepository delegationRepository;

    @MockBean
    public DelegationService delegationService;

    @Autowired
    OfferTableMapper offerTableMapper;

    @Autowired
    ProjectOfferItemMapper projectOfferItemMapper;

    @Autowired
    OfferConfirmationMapper offerConfirmationMapper;

    Optional<Delegation> delegation_op;

    @MockBean
    private Result<Item> singleFile;

    @MockBean
    private Item singleItem;

    private String goodDelegationId;

    private String badDelegationId;

    private HttpHeaders headers;

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

    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @BeforeEach
    void init()
    {
        goodDelegationId = "goodDelegationId";
        badDelegationId = "badDelegationId";
        Delegation delegation = getInitDelegation();
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
    }

    private String updateOfferUri = "/offer/delegation/{id}";

    @Test
    void updateOfferNotFound() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(null);
        mockMvc.perform(put(updateOfferUri, goodDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateOfferDisagree() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        OfferRequestDto offerRequestDto = new OfferRequestDto();
        offerRequestDto.set市场部态度("拒绝");
        String body = toJson(offerRequestDto);
        mockMvc.perform(put(updateOfferUri, goodDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isOk());
        Map<String, Object> variables = new HashMap<String, Object>();
        Delegation delegation = getInitDelegation();
        delegation.setState(DelegationState.AUDIT_MARKET_APARTMENT_DENIED);
        variables.put("state",2);
        verify(taskService, times(1)).complete(goodDelegationId, variables);
        verify(delegationRepository, times(1)).save(delegation);
    }

    @Test
    void updateOfferAgree() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        OfferRequestDto offerRequestDto = new OfferRequestDto();
        offerRequestDto.set市场部态度("同意");
        OfferTableDto offerTableDto = new OfferTableDto();
        offerRequestDto.set基本信息(offerTableDto);
        String body = toJson(offerRequestDto);
        mockMvc.perform(put(updateOfferUri, goodDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isOk());
        Map<String, Object> variables = new HashMap<String, Object>();
        Delegation delegation = getInitDelegation();
        delegation.setState(DelegationState.QUOTATION_USER);
        delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息())));
        variables.put("delegation",delegation);
        variables.put("state",1);
        //verify(delegationRepository, times(1)).save(eq(delegation));
        verify(taskService, times(1)).complete(eq(goodDelegationId), eq(variables));

    }

    private String createOfferUri = "/offer/delegation/{id}";
    @Test
    void createOfferOk() throws Exception {
        OfferRequestDto offerRequestDto = new OfferRequestDto();
        OfferTableDto offerTableDto = new OfferTableDto();
        offerRequestDto.set市场部态度(null);
        offerRequestDto.set基本信息(offerTableDto);
        Delegation delegation = getInitDelegation();
        delegation.setState(DelegationState.QUOTATION_USER);
        delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息())));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation",delegation);
        String body = toJson(offerRequestDto);
        mockMvc.perform(post(createOfferUri, goodDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isCreated());
        verify(runtimeService, times(1)).startProcessInstanceByKey("delegation_offer", variables);
    }

    @Test
    void createOfferNotFound() throws Exception {
        OfferRequestDto offerRequestDto = new OfferRequestDto();
        OfferTableDto offerTableDto = new OfferTableDto();
        offerRequestDto.set市场部态度(null);
        offerRequestDto.set基本信息(offerTableDto);
        Delegation delegation = getInitDelegation();
        delegation.setState(DelegationState.QUOTATION_USER);
        delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息())));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation",delegation);
        String body = toJson(offerRequestDto);
        mockMvc.perform(post(createOfferUri, badDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isNotFound());
        verify(runtimeService, times(0)).startProcessInstanceByKey("delegation_offer", variables);
    }

    private String replyOfferUri = "/offer/reply/delegation/{id}";
    @Test
    void replyOfferNotFound() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(null);
        OfferReplyRequestDto dto = new OfferReplyRequestDto();
        dto.set态度("state");
        OfferSignItemDto offerSignItemDto = new OfferSignItemDto();
        offerSignItemDto.set委托人签字("name");
        offerSignItemDto.set日期("data");
        dto.set确认信息(offerSignItemDto);
        dto.set附加信息("information");
        String body = toJson(dto);
        mockMvc.perform(post(replyOfferUri, badDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void replyOfferOk() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        OfferReplyRequestDto dto = new OfferReplyRequestDto();
        dto.set态度("state");
        OfferSignItemDto offerSignItemDto = new OfferSignItemDto();
        offerSignItemDto.set委托人签字("name");
        offerSignItemDto.set日期("data");
        dto.set确认信息(offerSignItemDto);
        dto.set附加信息("information");
        String body = toJson(dto);
        mockMvc.perform(post(replyOfferUri, goodDelegationId).contentType("application/json").headers(headers).content(body))
                .andExpect(status().isOk());
    }
}