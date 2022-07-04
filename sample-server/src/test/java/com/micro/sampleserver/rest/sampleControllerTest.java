package com.micro.sampleserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.model.DelegationState;
import com.micro.commonserver.service.MinioService;
import com.micro.dto.GetSampleResponseDto;
import com.micro.dto.SampleAcceptModelDto;
import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.SampleServerApplication;
import com.micro.sampleserver.model.Sample;
import com.micro.sampleserver.repository.MongoDBSampleAcceptRepository;
import com.micro.sampleserver.repository.MongoDBSampleRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
//import static org.springframework.test.web.servlet.MockMvcExtensionsKt.put;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SampleServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class sampleControllerTest {

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
    private MongoDBSampleRepository sampleRepository;
    @MockBean
    private Result<Item> singleFile;
    @MockBean
    private Item singleItem;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private MongoDBSampleAcceptRepository sampleAcceptRepository;
    private HttpHeaders headers;
    private MockMultipartFile mulFile;
    @BeforeEach
    void init(){
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
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        ArrayList<Task> list=new ArrayList<>();
        list.add(taskEntity);
        when(taskQuery.list())
                .thenReturn(list);
        when(taskEntity.getExecutionId())
                .thenReturn("sampleId");
        Iterable<Result<Item>> fileList = Arrays.asList(singleFile);
        when(minioService.listObjects(Mockito.anyString()))
                .thenReturn(fileList);
        try {
            when(singleFile.get())
                    .thenReturn(singleItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        when(singleItem.objectName())
                .thenReturn("fileName");
        try {
            when(minioService.getObjectURL(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn("http://goodUri");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Sample sample = new Sample();
        sample.setUsrId("usrId");
        sample.setDelegationId("sampleId");
        sample.setComment("comment");
        sample.setUsrName("usrName");
        when(sampleRepository.findById("sampleId"))
                .thenReturn(Optional.of(sample));
        when(sampleRepository.findById("badId"))
                .thenReturn(Optional.ofNullable(null));
        headers = new HttpHeaders();
        headers.set("usrName", "");
        headers.set("usrId", "");
        headers.set("usrRole", "");
        headers.set("Authorization", "");
        mulFile = new MockMultipartFile(
                "样品", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
    }

    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void changeOnlineSample() throws Exception
    {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/sample/online/{id}","sampleId").file(mulFile).headers(headers);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    private String setDelegationUri = "http://delegation-server//delegationServer/private/delegationState/";
    @Test
    void acceptSample() throws Exception
    {
//        SampleAcceptDto sampleAcceptDto = new SampleAcceptDto();
//        sampleAcceptDto.set态度("同意");
//        Map<String, Object> variables = new HashMap<String, Object>();
//        variables.put("isOk", 1);
//        DelegationState state = DelegationState.AUDIT_TEST_APARTMENT;
//        HttpEntity<String> request = new HttpEntity<>("", headers);
//        String body = toJson(sampleAcceptDto);
//        when(restTemplate.postForEntity(setDelegationUri + "sampleId" + "/" + state, request, Void.class))
//                .thenReturn(ResponseEntity.status(200).build());
//        mockMvc.perform(post("/sample/accept/{id}", "sampleId").contentType("application/json").headers(headers).content(body))
//                .andExpect(status().isOk());


    }

    @Test
    void changeOfflineSample() throws Exception
    {
        SampleMessageApplicationRequestDto dto = new SampleMessageApplicationRequestDto();
        dto.set备注("hello world");
        String body = toJson(dto);
        mockMvc.perform(put("/sample/offline/{id}", "sampleId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void createSampleFile() throws Exception
    {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/sample/online/{id}","sampleId").file(mulFile).headers(headers);
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
    void createSampleMessage() throws Exception
    {
        SampleMessageApplicationRequestDto dto = new SampleMessageApplicationRequestDto();
        dto.set备注("hello world");
        String body = toJson(dto);
        mockMvc.perform(post("/sample/offline/{id}", "sampleId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void getSample() throws Exception
    {
        GetSampleResponseDto content = new GetSampleResponseDto();
        content.setApplicationMethod("offline");
        content.setUri(null);
        content.setComment("comment");
        mockMvc.perform(get("/sample/{id}", "sampleId").contentType("application/json").headers(headers))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(content)));

        content.setApplicationMethod("online");
        content.setUri("http://goodUri");
        content.setComment(null);
        mockMvc.perform(get("/sample/{id}", "badId").contentType("application/json").headers(headers))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(content)));
    }

    @Test
    void deleteOfflineSample() throws Exception
    {
        mockMvc.perform(delete("/sample/offline/{id}", "sampleId").contentType("application/json").headers(headers))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOnlineSample() throws Exception
    {

        mockMvc.perform(delete("/sample/online/{id}", "sampleId").contentType("application/json").headers(headers))
                .andExpect(status().isOk());
    }
}