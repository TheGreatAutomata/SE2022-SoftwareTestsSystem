package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractTable;
import com.micro.contractserver.model.ContractTableExist;
import com.micro.contractserver.model.minioFileItem;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.contractserver.service.ContractService;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@Rollback
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SaveContractFilesDelegateTest {

    @Autowired
    private SaveContractFilesDelegate saveContractFilesDelegate;

    @MockBean
    private DelegateExecution delegateExecution;

    @MockBean
    private ContractService contractService;

    @MockBean
    private MongoDBContractRepository contractRepository;

    private MockMultipartFile signedContractFile;

    private MockMultipartFile signedNondisclosureAgreementFile;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

        contractRepository.deleteById("contractId");
        contractRepository.deleteById("wrongContractId");

    }

    @Test
    void execute() throws IOException {

        Contract contract = new Contract("delegationId", null);
        contract.setContractId("contractId");

        when(delegateExecution.getVariable("contractId"))
                .thenReturn("contractId");
        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        signedContractFile = new MockMultipartFile(
                "Contract_complete_contractId", //文件名
                "Contract_complete_contractId.pdf", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );

        signedNondisclosureAgreementFile = new MockMultipartFile(
                "NDA_complete_contractId", //文件名
                "NDA_complete_contractId.pdf", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );

        when(delegateExecution.getVariable("Contract_complete_contractId"))
                .thenReturn(signedContractFile.getBytes());
        when(delegateExecution.getVariable("Contract_complete_contractIdName"))
                .thenReturn(signedContractFile.getOriginalFilename());

        when(delegateExecution.getVariable("Contract_complete_wrongContractId"))
                .thenReturn(null);
        when(delegateExecution.getVariable("Contract_complete_wrongContractIdName"))
                .thenReturn("None");

        when(delegateExecution.getVariable("NDA_complete_contractId"))
                .thenReturn(signedNondisclosureAgreementFile.getBytes());
        when(delegateExecution.getVariable("NDA_complete_contractIdName"))
                .thenReturn(signedNondisclosureAgreementFile.getOriginalFilename());

        when(delegateExecution.getVariable("NDA_complete_wrongContractId"))
                .thenReturn(null);
        when(delegateExecution.getVariable("NDA_complete_wrongContractIdName"))
                .thenReturn("None");

        minioFileItem fileItem = new minioFileItem("111", "222", "333");

        when(contractService.getSignedContractTableFile("contractId"))
                .thenReturn(fileItem);
        when(contractService.getSignedNondisclosureAgreementTableFile("contractId"))
                .thenReturn(fileItem);

        saveContractFilesDelegate.execute(delegateExecution);

    }
}