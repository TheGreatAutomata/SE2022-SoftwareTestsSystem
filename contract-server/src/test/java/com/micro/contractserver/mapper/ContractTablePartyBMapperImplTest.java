package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTablePartyB;
import com.micro.dto.ContractTablePartyBDto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ContractTablePartyBMapperImplTest {

    private ContractTablePartyBMapperImpl contractTablePartyBMapper;

    private Collection<ContractTablePartyBDto> contractTablePartyBDtos;

    private Collection<ContractTablePartyB> contractTablePartyBs;

    private ContractTablePartyBDto contractTablePartyBDto = new ContractTablePartyBDto();

    private ContractTablePartyB contractTablePartyB = new ContractTablePartyB();

    @BeforeEach
    void setUp() {

        contractTablePartyBMapper = new ContractTablePartyBMapperImpl();

        contractTablePartyBDtos = new ArrayList<ContractTablePartyBDto>(Arrays.asList(contractTablePartyBDto));
        contractTablePartyBs = new ArrayList<ContractTablePartyB>(Arrays.asList(contractTablePartyB));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(contractTablePartyBDtos, contractTablePartyBMapper.toDtos(contractTablePartyBs));
        Assert.assertEquals(null, contractTablePartyBMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(contractTablePartyBs, contractTablePartyBMapper.toObjs(contractTablePartyBDtos));
        Assert.assertEquals(null, contractTablePartyBMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(contractTablePartyB, contractTablePartyBMapper.toObj(contractTablePartyBDto));
        Assert.assertEquals(null, contractTablePartyBMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(contractTablePartyBDto, contractTablePartyBMapper.toDto(contractTablePartyB));
        Assert.assertEquals(null, contractTablePartyBMapper.toDto(null));

    }
}