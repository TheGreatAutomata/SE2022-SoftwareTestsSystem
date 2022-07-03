package com.micro.contractserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.commonserver.service.MinioService;
import com.micro.contractserver.ContractServerApplication;
import com.micro.contractserver.mapper.ContractFileMapper;
import com.micro.contractserver.model.*;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.contractserver.service.ContractService;
import com.micro.dto.*;
import io.minio.Result;
import io.minio.messages.Item;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ContractServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class contractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContractService contractService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskQuery taskQuery;
    @MockBean
    private TaskEntity taskEntity;
    @MockBean
    private RuntimeService runtimeService;
    @MockBean
    private RepositoryService repositoryService;
    @MockBean
    private ProcessInstanceQuery processInstanceQuery;
    @MockBean
    private ProcessInstance processInstance;
    @MockBean
    private MinioService minioService;
    @MockBean
    private MongoDBContractRepository contractRepository;
    @MockBean
    private Result<Item> singleFile;
    @MockBean
    private Item singleItem;
    private HttpHeaders headers;
    private HttpHeaders headersWithDelegationId;
    private HttpHeaders headersWithWrongDelegationId;
    private MockMultipartFile mulFile;

    @BeforeEach
    void setUp() {

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
        ArrayList<ProcessInstance> instanceList = new ArrayList<>();
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
        ArrayList<Task> list = new ArrayList<>();
        list.add(taskEntity);
        when(taskQuery.list())
                .thenReturn(list);
        when(taskEntity.getExecutionId())
                .thenReturn("contractId");
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

        headers = new HttpHeaders();
        headers.set("Authorization", "token");
        headers.set("usrName", "usrName");
        headers.set("usrId", "usrId");
        headers.set("usrRole", "usrRole");

        headersWithDelegationId = new HttpHeaders();
        headersWithDelegationId.set("Authorization", "token");
        headersWithDelegationId.set("usrName", "usrName");
        headersWithDelegationId.set("usrId", "usrId");
        headersWithDelegationId.set("usrRole", "usrRole");
        headersWithDelegationId.set("delegationId", "delegationId");

        headersWithWrongDelegationId = new HttpHeaders();
        headersWithWrongDelegationId.set("Authorization", "token");
        headersWithWrongDelegationId.set("usrName", "usrName");
        headersWithWrongDelegationId.set("usrId", "usrId");
        headersWithWrongDelegationId.set("usrRole", "usrRole");
        headersWithWrongDelegationId.set("delegationId", "wrongDelegationId");

    }

    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void draftPerformanceTermPartyB() throws Exception {

        PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();
        String body = toJson(performanceTermPartyBDto);

        Contract contract = new Contract("delegationId", null);
        contract.setContractId("contractId");

        when(contractService.constructFromPerformanceTermPartyBDto("delegationId", performanceTermPartyBDto))
                .thenReturn(contract);


        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("contractId");
        String content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/performanceTerm/partyB").contentType("application/json").headers(headersWithDelegationId).content(body))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

    }

    @Test
    void getPerformanceTermPartyA() throws Exception {

        Contract contract = new Contract("delegationId", null);
        contract.setContractId("contractId");
        contract.setContractTable(new ContractTable(new ContractTableExist(), null, null));
        contract.getContractTable().getContractTableExist().setProjectName("projectName");
        contract.getContractTable().getContractTableExist().setPartyBName1("partyBName1");
        contract.getContractTable().getContractTableExist().setPerformanceTerm("performanceTerm");
        contract.getContractTable().getContractTableExist().setRectificationTimes("rectificationTimes");
        contract.getContractTable().getContractTableExist().setRectificationTerm("rectificationTerm");

        when(contractRepository.findByDelegationId("delegationId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByDelegationId("wrongDelegationId"))
                .thenReturn(Optional.ofNullable(null));

        PerformanceTermPartyAResponseDto performanceTermPartyAResponseDto = new PerformanceTermPartyAResponseDto();
        performanceTermPartyAResponseDto.setContractId("contractId");

        PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();
        performanceTermPartyBDto.set项目名称("projectName");
        performanceTermPartyBDto.set受托方乙方("partyBName1");
        performanceTermPartyBDto.set合同履行期限("performanceTerm");
        performanceTermPartyBDto.set整改限制次数("rectificationTimes");
        performanceTermPartyBDto.set一次整改限制的天数("rectificationTerm");

        performanceTermPartyAResponseDto.set履行期限受托方部分(performanceTermPartyBDto);

        String content = toJson(performanceTermPartyAResponseDto);

        mockMvc.perform(get("/contract/performanceTerm/partyA").contentType("application/json").headers(headersWithDelegationId))
                .andExpect(content().json(content))
                .andExpect(status().isOk());
        mockMvc.perform(get("/contract/performanceTerm/partyA").contentType("application/json").headers(headersWithWrongDelegationId))
                .andExpect(status().isBadRequest());

    }

    @Test
    void replyPerformanceTermPartyA() throws Exception {

        Contract contract = new Contract("delegationId", null);
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        PerformanceTermPartyADto performanceTermPartyADto = new PerformanceTermPartyADto();
        performanceTermPartyADto.set态度("接受");
        performanceTermPartyADto.set意见("接受履行期限");

        String body = toJson(performanceTermPartyADto);

        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("reply successfully");
        String content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/performanceTerm/partyA", "contractId").contentType("application/json").headers(headers).content(body))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

        normalResponseDto.setResponseInfo("contract not found");
        content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/performanceTerm/partyA", "wrongContractId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getPerformanceTermReplyPartyB() throws Exception {

        Contract contract = new Contract("delegationId", null);
        contract.setContractId("contractId");
        contract.setPerformanceTermState("PerformanceTermState");
        contract.setPerformanceTermSuggestion("PerformanceTermSuggestion");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        PerformanceTermPartyADto performanceTermPartyADto = new PerformanceTermPartyADto();
        performanceTermPartyADto.set态度("PerformanceTermState");
        performanceTermPartyADto.set意见("PerformanceTermSuggestion");

        String content = toJson(performanceTermPartyADto);

        mockMvc.perform(get("/contract/{id}/performanceTerm/partyB", "contractId").contentType("application/json").headers(headers))
                .andExpect(content().json(content))
                .andExpect(status().isOk());
        mockMvc.perform(get("/contract/{id}/performanceTerm/partyB", "wrongContractId").contentType("application/json").headers(headers))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updatePerformanceTermPartyB() throws Exception {

        Contract contract = new Contract("delegationId", new ContractTable(new ContractTableExist(), null, null));
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();
        performanceTermPartyBDto.set项目名称("项目名称");
        performanceTermPartyBDto.set受托方乙方("受托方乙方");
        performanceTermPartyBDto.set合同履行期限("合同履行期限");
        performanceTermPartyBDto.set整改限制次数("整改限制次数");
        performanceTermPartyBDto.set一次整改限制的天数("一次整改限制的天数");

        String body = toJson(performanceTermPartyBDto);

        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("update successfully");
        String content = toJson(normalResponseDto);

        mockMvc.perform(put("/contract/{id}/performanceTerm/partyB", "contractId").contentType("application/json").headers(headers).content(body))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

        normalResponseDto.setResponseInfo("contract not found");
        content = toJson(normalResponseDto);

        mockMvc.perform(put("/contract/{id}/performanceTerm/partyB", "wrongContractId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void addContractTablePartyB() throws Exception {

        Contract contract = new Contract("delegationId", new ContractTable(null, null, new ContractTablePartyB()));
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        ContractTablePartyBDto contractTablePartyBDto = new ContractTablePartyBDto();

        String body = toJson(contractTablePartyBDto);

        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("add successfully");
        String content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/contractTable/partyB", "contractId").contentType("application/json").headers(headers).content(body))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

        normalResponseDto.setResponseInfo("contract not found");
        content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/contractTable/partyB", "wrongContractId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getContractTablePartyA() throws Exception {

        Contract contract = new Contract("delegationId", new ContractTable(null, null, new ContractTablePartyB()));
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        ContractTablePartyBDto contractTablePartyBDto = new ContractTablePartyBDto();

        String content = toJson(contractTablePartyBDto);

        mockMvc.perform(get("/contract/{id}/contractTable/partyA", "contractId").contentType("application/json").headers(headers))
                .andExpect(content().json(content))
                .andExpect(status().isOk());
        mockMvc.perform(get("/contract/{id}/contractTable/partyA", "wrongContractId").contentType("application/json").headers(headers))
                .andExpect(status().isBadRequest());

    }

    @Test
    void addContractTablePartyA() throws Exception {

        Contract contract = new Contract("delegationId", new ContractTable(new ContractTableExist(), null, null));
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        ContractTablePartyADto contractTablePartyADto = new ContractTablePartyADto();

        String body = toJson(contractTablePartyADto);

        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("add successfully");
        String content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/contractTable/partyA", "contractId").contentType("application/json").headers(headers).content(body))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

        normalResponseDto.setResponseInfo("contract not found");
        content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/contractTable/partyA", "wrongContractId").contentType("application/json").headers(headers).content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void downloadUnsignedContractTablePartyB() throws Exception {

        minioFileItem fileItem = new minioFileItem();

        when(contractService.getUnsignedContractTableFile("contractId"))
                .thenReturn(fileItem);
        when(contractService.getUnsignedContractTableFile("wrongContractId"))
                .thenReturn(null);

        ContractFileMapper contractFileMapper = new ContractFileMapper();

        SingleFileDto singleFileDto = contractFileMapper.toSingleFileDto(fileItem);

        String content = toJson(singleFileDto);

        mockMvc.perform(get("/contract/{id}/files/unsignedContractTable", "contractId").contentType("application/json").headers(headers))
                .andExpect(content().json(content))
                .andExpect(status().isOk());
        mockMvc.perform(get("/contract/{id}/files/unsignedContractTable", "wrongContractId").contentType("application/json").headers(headers))
                .andExpect(status().isBadRequest());

    }

    @Test
    void downloadUnsignedNondisclosureAgreementTablePartyB() throws Exception {

        minioFileItem fileItem = new minioFileItem();

        when(contractService.getUnsignedNondisclosureAgreementTableFile("contractId"))
                .thenReturn(fileItem);
        when(contractService.getUnsignedNondisclosureAgreementTableFile("wrongContractId"))
                .thenReturn(null);

        ContractFileMapper contractFileMapper = new ContractFileMapper();

        SingleFileDto singleFileDto = contractFileMapper.toSingleFileDto(fileItem);

        String content = toJson(singleFileDto);

        mockMvc.perform(get("/contract/{id}/files/unsignedNondisclosureAgreementTable", "contractId").contentType("application/json").headers(headers))
                .andExpect(content().json(content))
                .andExpect(status().isOk());
        mockMvc.perform(get("/contract/{id}/files/unsignedNondisclosureAgreementTable", "wrongContractId").contentType("application/json").headers(headers))
                .andExpect(status().isBadRequest());

    }

    @Test
    void uploadContractPartyB() throws Exception {

        Contract contract = new Contract("delegationId", new ContractTable(new ContractTableExist(), null, null));
        contract.setContractId("contractId");

        when(contractRepository.findByContractId("contractId"))
                .thenReturn(Optional.of(contract));
        when(contractRepository.findByContractId("wrongContractId"))
                .thenReturn(Optional.ofNullable(null));

        mulFile = new MockMultipartFile(
                "样品", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/contract/{id}/files","contractId");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("POST");
                return request;
            }
        });

        NormalResponseDto normalResponseDto = new NormalResponseDto();
        normalResponseDto.setResponseInfo("add successfully");
        String content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/files", "contractId").contentType("application/json").headers(headers))
                .andExpect(content().json(content))
                .andExpect(status().isOk());

        normalResponseDto.setResponseInfo("contract not found");
        content = toJson(normalResponseDto);

        mockMvc.perform(post("/contract/{id}/files", "wrongContractId").contentType("application/json").headers(headers))
                .andExpect(status().isBadRequest());

    }

    @Test
    void downloadSignedContractTable() {
    }

    @Test
    void downloadSignedNondisclosureAgreementTable() {
    }

    @Test
    void getContractByContractId() {
    }

    @Test
    void getContractByDelegationId() {
    }

}