package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTablePartyA;
import com.micro.dto.ContractTablePartyADto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ContractTablePartyAMapperImplTest {

    private ContractTablePartyAMapperImpl contractTablePartyAMapper;

    private Collection<ContractTablePartyADto> contractTablePartyADtos;

    private Collection<ContractTablePartyA> contractTablePartyAs;

    private ContractTablePartyADto contractTablePartyADto;

    private ContractTablePartyA contractTablePartyA;

    @BeforeEach
    void setUp() {

        contractTablePartyAMapper = new ContractTablePartyAMapperImpl();

        contractTablePartyADtos = new ArrayList<ContractTablePartyADto>(Arrays.asList(contractTablePartyADto));
        contractTablePartyAs = new ArrayList<ContractTablePartyA>(Arrays.asList(contractTablePartyA));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(contractTablePartyADtos, contractTablePartyAMapper.toDtos(contractTablePartyAs));
        Assert.assertEquals(null, contractTablePartyAMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(contractTablePartyAs, contractTablePartyAMapper.toObjs(contractTablePartyADtos));
        Assert.assertEquals(null, contractTablePartyAMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(contractTablePartyA, contractTablePartyAMapper.toObj(contractTablePartyADto));
        Assert.assertEquals(null, contractTablePartyAMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(contractTablePartyADto, contractTablePartyAMapper.toDto(contractTablePartyA));
        Assert.assertEquals(null, contractTablePartyAMapper.toDto(null));

    }
}