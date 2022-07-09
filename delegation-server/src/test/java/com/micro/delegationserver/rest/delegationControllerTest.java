package com.micro.delegationserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.commonserver.service.MinioService;
import com.micro.delegationserver.DelegationServerApplication;
import com.micro.delegationserver.mapper.DelegationFilesMapper;
import com.micro.delegationserver.mapper.OfferConfirmationMapper;
import com.micro.delegationserver.mapper.OfferTableMapper;
import com.micro.delegationserver.mapper.ProjectOfferItemMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.delegationserver.model.OfferTableUnion;
import com.micro.delegationserver.model.minioFileItem;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DelegationServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class delegationControllerTest {
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

    private String createDelegationFileUri = "/delegation/{id}/files";

    @Test
    void createDelegationFileNotFound() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(null);
        MockMultipartFile mulFile1;
        mulFile1 = new MockMultipartFile(
                "usrManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile2;
        mulFile2 = new MockMultipartFile(
                "installationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile3;
        mulFile3 = new MockMultipartFile(
                "operationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile4;
        mulFile4 = new MockMultipartFile(
                "maintenanceManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(createDelegationFileUri,badDelegationId)
                        .file(mulFile1)
                        .file(mulFile2)
                        .file(mulFile3)
                        .file(mulFile4)
                        .headers(headers);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });
        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

    }

    @Test
    void createDelegationFileOk() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        MockMultipartFile mulFile1;
        mulFile1 = new MockMultipartFile(
                "usrManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile2;
        mulFile2 = new MockMultipartFile(
                "installationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile3;
        mulFile3 = new MockMultipartFile(
                "operationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile4;
        mulFile4 = new MockMultipartFile(
                "maintenanceManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(createDelegationFileUri,goodDelegationId)
                        .file(mulFile1)
                        .file(mulFile2)
                        .file(mulFile3)
                        .file(mulFile4)
                        .headers(headers);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });
        mockMvc.perform(builder)
                .andExpect(status().isCreated());
    }

    @Test
    void createDelegationFileForNoFile() throws Exception {
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        MockMultipartFile mulFile1;
        mulFile1 = new MockMultipartFile(
                "usrManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile2;
        mulFile2 = new MockMultipartFile(
                "installationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile3;
        mulFile3 = new MockMultipartFile(
                "operationManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockMultipartFile mulFile4;
        mulFile4 = new MockMultipartFile(
                "maintenanceManual", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(createDelegationFileUri,goodDelegationId)
                        .headers(headers);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });
        mockMvc.perform(builder)
                .andExpect(status().isCreated());
    }

    private String listDelegationFileUri = "/delegation/{id}/files";
    @Test
    void listDelegationFileNotFound() throws Exception {
        when(delegationService.getAllFiles("delega" + badDelegationId))
                .thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(listDelegationFileUri, badDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void listDelegationFileOk() throws Exception {
        minioFileItem item = new minioFileItem();
        item.setFileName("name");
        item.setFileType("png");
        item.setFileUri("www.uri.com");
        List<minioFileItem> itemList = new ArrayList<>();
        itemList.add(item);
        when(delegationService.getAllFiles("delega" + goodDelegationId))
                .thenReturn(itemList);
        mockMvc.perform(MockMvcRequestBuilders.get(listDelegationFileUri, badDelegationId).contentType("application/json").headers(headers).content(""))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDelegation()
    {

    }
}