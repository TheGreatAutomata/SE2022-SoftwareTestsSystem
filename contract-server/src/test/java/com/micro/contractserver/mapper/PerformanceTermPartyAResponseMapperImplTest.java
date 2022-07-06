package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyAResponse;
import com.micro.contractserver.model.PerformanceTermPartyB;
import com.micro.dto.PerformanceTermPartyAResponseDto;
import com.micro.dto.PerformanceTermPartyBDto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTermPartyAResponseMapperImplTest {

    private PerformanceTermPartyAResponseMapperImpl performanceTermPartyAResponseMapper;

    private Collection<PerformanceTermPartyAResponseDto> performanceTermPartyAResponseDtos;

    private Collection<PerformanceTermPartyAResponse> performanceTermPartyAResponses;

    private PerformanceTermPartyAResponseDto performanceTermPartyAResponseDto = new PerformanceTermPartyAResponseDto();

    private PerformanceTermPartyAResponse performanceTermPartyAResponse = new PerformanceTermPartyAResponse();

    private PerformanceTermPartyBDto performanceTermPartyBDto = new PerformanceTermPartyBDto();

    private PerformanceTermPartyB performanceTermPartyB = new PerformanceTermPartyB();

    @BeforeEach
    void setUp() {

        performanceTermPartyAResponseMapper = new PerformanceTermPartyAResponseMapperImpl();

        performanceTermPartyAResponseDtos = new ArrayList<PerformanceTermPartyAResponseDto>(Arrays.asList(performanceTermPartyAResponseDto));
        performanceTermPartyAResponses = new ArrayList<PerformanceTermPartyAResponse>(Arrays.asList(performanceTermPartyAResponse));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(performanceTermPartyAResponseDtos, performanceTermPartyAResponseMapper.toDtos(performanceTermPartyAResponses));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(performanceTermPartyAResponses, performanceTermPartyAResponseMapper.toObjs(performanceTermPartyAResponseDtos));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(performanceTermPartyAResponse, performanceTermPartyAResponseMapper.toObj(performanceTermPartyAResponseDto));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(performanceTermPartyAResponseDto, performanceTermPartyAResponseMapper.toDto(performanceTermPartyAResponse));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.toDto(null));

    }

    @Test
    void performanceTermPartyBDtoToPerformanceTermPartyB() {

        Assert.assertEquals(performanceTermPartyB, performanceTermPartyAResponseMapper.performanceTermPartyBDtoToPerformanceTermPartyB(performanceTermPartyBDto));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.performanceTermPartyBDtoToPerformanceTermPartyB(null));

    }

    @Test
    void performanceTermPartyBToPerformanceTermPartyBDto() {

        Assert.assertEquals(performanceTermPartyBDto, performanceTermPartyAResponseMapper.performanceTermPartyBToPerformanceTermPartyBDto(performanceTermPartyB));
        Assert.assertEquals(null, performanceTermPartyAResponseMapper.performanceTermPartyBToPerformanceTermPartyBDto(null));

    }
}