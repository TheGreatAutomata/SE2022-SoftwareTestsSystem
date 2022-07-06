package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyB;
import com.micro.dto.PerformanceTermPartyBDto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTermPartyBMapperImplTest {

    private PerformanceTermPartyBMapperImpl performanceTermPartyBMapper;

    private Collection<PerformanceTermPartyB> performanceTermPartyBs;

    private Collection<PerformanceTermPartyBDto> performanceTermPartyBDtos;

    private PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();

    private PerformanceTermPartyB performanceTermPartyB = new PerformanceTermPartyB();

    @BeforeEach
    void setUp() {

        performanceTermPartyBMapper = new PerformanceTermPartyBMapperImpl();

        performanceTermPartyBDtos = new ArrayList<PerformanceTermPartyBDto>(Arrays.asList(performanceTermPartyBDto));
        performanceTermPartyBs = new ArrayList<PerformanceTermPartyB>(Arrays.asList(performanceTermPartyB));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(performanceTermPartyBDtos, performanceTermPartyBMapper.toDtos(performanceTermPartyBs));
        Assert.assertEquals(null, performanceTermPartyBMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(performanceTermPartyBs, performanceTermPartyBMapper.toObjs(performanceTermPartyBDtos));
        Assert.assertEquals(null, performanceTermPartyBMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(performanceTermPartyB, performanceTermPartyBMapper.toObj(performanceTermPartyBDto));
        Assert.assertEquals(null, performanceTermPartyBMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(performanceTermPartyBDto, performanceTermPartyBMapper.toDto(performanceTermPartyB));
        Assert.assertEquals(null, performanceTermPartyBMapper.toDto(null));

    }
}