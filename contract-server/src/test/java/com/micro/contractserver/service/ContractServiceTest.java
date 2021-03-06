package com.micro.contractserver.service;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.micro.commonserver.service.MinioService;
import com.micro.contractserver.ContractServerApplication;
import com.micro.contractserver.mapper.ContractTablePartyBMapper;
import com.micro.contractserver.mapper.ContractTablePartyBMapperImpl;
import com.micro.contractserver.mapper.PerformanceTermPartyBMapper;
import com.micro.contractserver.mapper.PerformanceTermPartyBMapperImpl;
import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractTable;
import com.micro.contractserver.model.ContractTableExist;
import com.micro.contractserver.model.PerformanceTermPartyB;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.dto.PerformanceTermPartyBDto;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ContractService.class)
@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Autowired
    private ContractService contractService;

    @MockBean
    private PerformanceTermPartyBMapper performanceTermPartyBMapper;

    @MockBean
    private MinioService minioService;

    @MockBean
    private MongoDBContractRepository contractRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void constructFromPerformanceTermPartyBDto() {

        PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();
        performanceTermPartyBDto.set????????????("????????????");
        performanceTermPartyBDto.set???????????????("???????????????");
        performanceTermPartyBDto.set??????????????????("??????????????????");
        performanceTermPartyBDto.set??????????????????("??????????????????");
        performanceTermPartyBDto.set???????????????????????????("???????????????????????????");

        when(performanceTermPartyBMapper.toObj(performanceTermPartyBDto))
                .thenReturn(new PerformanceTermPartyBMapperImpl().toObj(performanceTermPartyBDto));

        Contract contract = new Contract("delegationId", new ContractTable(new ContractTableExist(performanceTermPartyBDto.get????????????(), performanceTermPartyBDto.get???????????????(), performanceTermPartyBDto.get???????????????(), performanceTermPartyBDto.get???????????????(), performanceTermPartyBDto.get??????????????????(), performanceTermPartyBDto.get??????????????????(), performanceTermPartyBDto.get???????????????????????????()), null, null));

        Contract currentContract = contractService.constructFromPerformanceTermPartyBDto("delegationId", performanceTermPartyBDto);

        Assert.assertEquals(contract.getDelegationId(), currentContract.getDelegationId());
        Assert.assertEquals(contract.getContractTable(), currentContract.getContractTable());

    }

    @Test
    void creatFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "Contract_complete", //?????????
                "Contract_complete.pdf", //originalName ????????????????????????????????????????????????
                MediaType.TEXT_PLAIN_VALUE, //????????????
                "Hello, World!".getBytes() //?????????
        );

        MockMultipartFile wrongFile = null;

        when(minioService.getObjectInfo("contractId", "Contract_complete.pdf"))
                .thenThrow(new NullPointerException());

        contractService.creatFile("contractId", "Contract_complete.pdf", "Contract_complete", file);

        minioService.removeBucket("contractId");

    }

    @Test
    void getUnsignedContractTableFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "Contract_contractId", //?????????
                "Contract_contractId.pdf", //originalName ????????????????????????????????????????????????
                MediaType.TEXT_PLAIN_VALUE, //????????????
                "Hello, World!".getBytes() //?????????
        );

        contractService.creatFile("contractId", "Contract_contractId.pdf", "Contract_contractId", file);

        contractService.getUnsignedContractTableFile("contractId");

        when(minioService.listObjects("wrongContractId"))
                .thenReturn(null);

        contractService.getUnsignedContractTableFile("wrongContractId");

        minioService.removeObject("contractId", "Contract_contractId.pdf");
        minioService.removeBucket("contractId");

    }

    @Test
    void getUnsignedNondisclosureAgreementTableFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "NDA_contractId", //?????????
                "NDA_contractId.pdf", //originalName ????????????????????????????????????????????????
                MediaType.TEXT_PLAIN_VALUE, //????????????
                "Hello, World!".getBytes() //?????????
        );

        contractService.creatFile("contractId", "NDA_contractId.pdf", "NDA_contractId", file);

        contractService.getUnsignedNondisclosureAgreementTableFile("contractId");

        when(minioService.listObjects("wrongContractId"))
                .thenReturn(null);

        contractService.getUnsignedNondisclosureAgreementTableFile("wrongContractId");

        minioService.removeObject("contractId", "NDA_contractId.pdf");
        minioService.removeBucket("contractId");

    }

    @Test
    void getSignedContractTableFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "Contract_complete_contractId", //?????????
                "Contract_complete_contractId.pdf", //originalName ????????????????????????????????????????????????
                MediaType.TEXT_PLAIN_VALUE, //????????????
                "Hello, World!".getBytes() //?????????
        );

        contractService.creatFile("contractId", "Contract_complete_contractId.pdf", "Contract_complete_contractId", file);

        contractService.getSignedContractTableFile("contractId");

        when(minioService.listObjects("wrongContractId"))
                .thenReturn(null);

        contractService.getSignedContractTableFile("wrongContractId");

        minioService.removeObject("contractId", "Contract_complete_contractId.pdf");
        minioService.removeBucket("contractId");

    }

    @Test
    void getSignedNondisclosureAgreementTableFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "NDA_complete_contractId", //?????????
                "NDA_complete_contractId.pdf", //originalName ????????????????????????????????????????????????
                MediaType.TEXT_PLAIN_VALUE, //????????????
                "Hello, World!".getBytes() //?????????
        );

        contractService.creatFile("contractId", "NDA_complete_contractId.pdf", "NDA_complete_contractId", file);

        contractService.getSignedNondisclosureAgreementTableFile("contractId");

        when(minioService.listObjects("wrongContractId"))
                .thenReturn(null);

        contractService.getSignedNondisclosureAgreementTableFile("wrongContractId");

        minioService.removeObject("contractId", "NDA_complete_contractId.pdf");
        minioService.removeBucket("contractId");

    }
}