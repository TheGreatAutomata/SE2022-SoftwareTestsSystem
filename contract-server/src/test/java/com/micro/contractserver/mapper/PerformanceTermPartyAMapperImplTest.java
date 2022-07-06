package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyA;
import com.micro.dto.PerformanceTermPartyADto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTermPartyAMapperImplTest {

    private PerformanceTermPartyAMapperImpl performanceTermPartyAMapper;

    private Collection<PerformanceTermPartyADto> performanceTermPartyADtos;

    private Collection<PerformanceTermPartyA> performanceTermPartyAs;

    private PerformanceTermPartyADto performanceTermPartyADto = new PerformanceTermPartyADto();

    private PerformanceTermPartyA performanceTermPartyA = new PerformanceTermPartyA();

    @BeforeEach
    void setUp() {

        performanceTermPartyAMapper = new PerformanceTermPartyAMapperImpl();

        performanceTermPartyADtos = new ArrayList<PerformanceTermPartyADto>(Arrays.asList(performanceTermPartyADto));
        performanceTermPartyAs = new ArrayList<PerformanceTermPartyA>(Arrays.asList(performanceTermPartyA));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(performanceTermPartyADtos, performanceTermPartyAMapper.toDtos(performanceTermPartyAs));
        Assert.assertEquals(null, performanceTermPartyAMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(performanceTermPartyAs, performanceTermPartyAMapper.toObjs(performanceTermPartyADtos));
        Assert.assertEquals(null, performanceTermPartyAMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(performanceTermPartyA, performanceTermPartyAMapper.toObj(performanceTermPartyADto));
        Assert.assertEquals(null, performanceTermPartyAMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(performanceTermPartyADto, performanceTermPartyAMapper.toDto(performanceTermPartyA));
        Assert.assertEquals(null, performanceTermPartyAMapper.toDto(null));

    }
}