package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTable;
import com.micro.contractserver.model.ContractTableExist;
import com.micro.contractserver.model.ContractTablePartyA;
import com.micro.contractserver.model.ContractTablePartyB;
import com.micro.dto.ContractTableDto;
import com.micro.dto.ContractTableExistDto;
import com.micro.dto.ContractTablePartyADto;
import com.micro.dto.ContractTablePartyBDto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ContractTableMapperImplTest {

    private ContractTableMapperImpl contractTableMapper;

    private Collection<ContractTableDto> contractTableDtos;

    private Collection<ContractTable> contractTables;

    private ContractTableDto contractTableDto = new ContractTableDto();

    private ContractTable contractTable = new ContractTable();

    private ContractTableExistDto contractTableExistDto = new ContractTableExistDto();

    private ContractTableExist contractTableExist = new ContractTableExist();

    private ContractTablePartyADto contractTablePartyADto = new ContractTablePartyADto();

    private ContractTablePartyA contractTablePartyA = new ContractTablePartyA();

    private ContractTablePartyBDto contractTablePartyBDto = new ContractTablePartyBDto();

    private ContractTablePartyB contractTablePartyB = new ContractTablePartyB();

    @BeforeEach
    void setUp() {

        contractTableMapper = new ContractTableMapperImpl();

        contractTableDtos = new ArrayList<ContractTableDto>(Arrays.asList(contractTableDto));
        contractTables = new ArrayList<ContractTable>(Arrays.asList(contractTable));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(contractTableDtos, contractTableMapper.toDtos(contractTables));
        Assert.assertEquals(null, contractTableMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(contractTables, contractTableMapper.toObjs(contractTableDtos));
        Assert.assertEquals(null, contractTableMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(contractTable, contractTableMapper.toObj(contractTableDto));
        Assert.assertEquals(null, contractTableMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(contractTableDto, contractTableMapper.toDto(contractTable));
        Assert.assertEquals(null, contractTableMapper.toDto(null));

    }

    @Test
    void contractTableExistDtoToContractTableExist() {

        Assert.assertEquals(contractTableExist, contractTableMapper.contractTableExistDtoToContractTableExist(contractTableExistDto));
        Assert.assertEquals(null, contractTableMapper.contractTableExistDtoToContractTableExist(null));

    }

    @Test
    void contractTablePartyADtoToContractTablePartyA() {

        Assert.assertEquals(contractTablePartyA, contractTableMapper.contractTablePartyADtoToContractTablePartyA(contractTablePartyADto));
        Assert.assertEquals(null, contractTableMapper.contractTablePartyADtoToContractTablePartyA(null));

    }

    @Test
    void contractTablePartyBDtoToContractTablePartyB() {

        Assert.assertEquals(contractTablePartyB, contractTableMapper.contractTablePartyBDtoToContractTablePartyB(contractTablePartyBDto));
        Assert.assertEquals(null, contractTableMapper.contractTablePartyBDtoToContractTablePartyB(null));

    }

    @Test
    void contractTableExistToContractTableExistDto() {

        Assert.assertEquals(contractTableExistDto, contractTableMapper.contractTableExistToContractTableExistDto(contractTableExist));
        Assert.assertEquals(null, contractTableMapper.contractTableExistToContractTableExistDto(null));

    }

    @Test
    void contractTablePartyAToContractTablePartyADto() {

        Assert.assertEquals(contractTablePartyADto, contractTableMapper.contractTablePartyAToContractTablePartyADto(contractTablePartyA));
        Assert.assertEquals(null, contractTableMapper.contractTablePartyAToContractTablePartyADto(null));

    }

    @Test
    void contractTablePartyBToContractTablePartyBDto() {

        Assert.assertEquals(contractTablePartyBDto, contractTableMapper.contractTablePartyBToContractTablePartyBDto(contractTablePartyB));
        Assert.assertEquals(null, contractTableMapper.contractTablePartyBToContractTablePartyBDto(null));

    }
}