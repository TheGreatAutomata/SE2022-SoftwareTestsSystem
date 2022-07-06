package com.micro.contractserver.mapper;

import com.micro.contractserver.model.*;
import com.micro.dto.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ContractMapperImplTest {

    private ContractMapperImpl contractMapper;

    private Collection<ContractDto> contractDtos;

    private Collection<Contract> contracts;

    private ContractDto contractDto = new ContractDto();

    private Contract contract = new Contract();

    private ContractTableExistDto contractTableExistDto = new ContractTableExistDto();

    private ContractTableExist contractTableExist = new ContractTableExist();

    private ContractTablePartyADto contractTablePartyADto = new ContractTablePartyADto();

    private ContractTablePartyA contractTablePartyA = new ContractTablePartyA();

    private ContractTablePartyBDto contractTablePartyBDto = new ContractTablePartyBDto();

    private ContractTablePartyB contractTablePartyB = new ContractTablePartyB();

    private ContractTableDto contractTableDto = new ContractTableDto();

    private ContractTable contractTable = new ContractTable();

    private NondisclosureAgreementTableDto nondisclosureAgreementTableDto = new NondisclosureAgreementTableDto();

    private NondisclosureAgreementTable nondisclosureAgreementTable = new NondisclosureAgreementTable();

    private SingleFileDto singleFileDto = new SingleFileDto();

    private minioFileItem minioFileItem = new minioFileItem();


    @BeforeEach
    void setUp() {

        contractMapper = new ContractMapperImpl();

        contractDtos = new ArrayList<ContractDto>(Arrays.asList(contractDto));
        contracts = new ArrayList<Contract>(Arrays.asList(contract));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(contractDtos, contractMapper.toDtos(contracts));
        Assert.assertEquals(null, contractMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(contracts, contractMapper.toObjs(contractDtos));
        Assert.assertEquals(null, contractMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(contract, contractMapper.toObj(contractDto));
        Assert.assertEquals(null, contractMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(contractDto, contractMapper.toDto(contract));
        Assert.assertEquals(null, contractMapper.toDto(null));

    }

    @Test
    void contractTableExistDtoToContractTableExist() {

        Assert.assertEquals(contractTableExist, contractMapper.contractTableExistDtoToContractTableExist(contractTableExistDto));
        Assert.assertEquals(null, contractMapper.contractTableExistDtoToContractTableExist(null));

    }

    @Test
    void contractTablePartyADtoToContractTablePartyA() {

        Assert.assertEquals(contractTablePartyA, contractMapper.contractTablePartyADtoToContractTablePartyA(contractTablePartyADto));
        Assert.assertEquals(null, contractMapper.contractTablePartyADtoToContractTablePartyA(null));

    }

    @Test
    void contractTablePartyBDtoToContractTablePartyB() {

        Assert.assertEquals(contractTablePartyB, contractMapper.contractTablePartyBDtoToContractTablePartyB(contractTablePartyBDto));
        Assert.assertEquals(null, contractMapper.contractTablePartyBDtoToContractTablePartyB(null));

    }

    @Test
    void contractTableDtoToContractTable() {

        Assert.assertEquals(contractTable, contractMapper.contractTableDtoToContractTable(contractTableDto));
        Assert.assertEquals(null, contractMapper.contractTableDtoToContractTable(null));

    }

    @Test
    void nondisclosureAgreementTableDtoToNondisclosureAgreementTable() {

        Assert.assertEquals(nondisclosureAgreementTable, contractMapper.nondisclosureAgreementTableDtoToNondisclosureAgreementTable(nondisclosureAgreementTableDto));
        Assert.assertEquals(null, contractMapper.nondisclosureAgreementTableDtoToNondisclosureAgreementTable(null));

    }

    @Test
    void singleFileDtoTominioFileItem() {

        Assert.assertEquals(minioFileItem, contractMapper.singleFileDtoTominioFileItem(singleFileDto));
        Assert.assertEquals(null, contractMapper.singleFileDtoTominioFileItem(null));

    }

    @Test
    void contractTableExistToContractTableExistDto() {

        Assert.assertEquals(contractTableExistDto, contractMapper.contractTableExistToContractTableExistDto(contractTableExist));
        Assert.assertEquals(null, contractMapper.contractTableExistToContractTableExistDto(null));

    }

    @Test
    void contractTablePartyAToContractTablePartyADto() {

        Assert.assertEquals(contractTablePartyADto, contractMapper.contractTablePartyAToContractTablePartyADto(contractTablePartyA));
        Assert.assertEquals(null, contractMapper.contractTablePartyAToContractTablePartyADto(null));

    }

    @Test
    void contractTablePartyBToContractTablePartyBDto() {

        Assert.assertEquals(contractTablePartyBDto, contractMapper.contractTablePartyBToContractTablePartyBDto(contractTablePartyB));
        Assert.assertEquals(null, contractMapper.contractTablePartyBToContractTablePartyBDto(null));

    }

    @Test
    void contractTableToContractTableDto() {

        Assert.assertEquals(contractTableDto, contractMapper.contractTableToContractTableDto(contractTable));
        Assert.assertEquals(null, contractMapper.contractTableToContractTableDto(null));

    }

    @Test
    void nondisclosureAgreementTableToNondisclosureAgreementTableDto() {

        Assert.assertEquals(nondisclosureAgreementTableDto, contractMapper.nondisclosureAgreementTableToNondisclosureAgreementTableDto(nondisclosureAgreementTable));
        Assert.assertEquals(null, contractMapper.nondisclosureAgreementTableToNondisclosureAgreementTableDto(null));

    }

    @Test
    void minioFileItemToSingleFileDto() {

        Assert.assertEquals(singleFileDto, contractMapper.minioFileItemToSingleFileDto(minioFileItem));
        Assert.assertEquals(null, contractMapper.minioFileItemToSingleFileDto(null));

    }
}