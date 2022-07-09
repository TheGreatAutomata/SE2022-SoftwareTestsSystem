package com.micro.contractserver.mapper;

import com.micro.contractserver.model.NormalResponse;
import com.micro.dto.NormalResponseDto;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class NormalResponseMapperImplTest {

    private NormalResponseMapperImpl normalResponseMapper;

    private Collection<NormalResponseDto> normalResponseDtos;

    private Collection<NormalResponse> normalResponses;

    private NormalResponseDto normalResponseDto = new NormalResponseDto();

    private NormalResponse normalResponse = new NormalResponse();

    @BeforeEach
    void setUp() {

        normalResponseMapper = new NormalResponseMapperImpl();

        normalResponseDtos = new ArrayList<NormalResponseDto>(Arrays.asList(normalResponseDto));
        normalResponses = new ArrayList<NormalResponse>(Arrays.asList(normalResponse));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(normalResponseDtos, normalResponseMapper.toDtos(normalResponses));
        Assert.assertEquals(null, normalResponseMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(normalResponses, normalResponseMapper.toObjs(normalResponseDtos));
        Assert.assertEquals(null, normalResponseMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(normalResponse, normalResponseMapper.toObj(normalResponseDto));
        Assert.assertEquals(null, normalResponseMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(normalResponseDto, normalResponseMapper.toDto(normalResponse));
        Assert.assertEquals(null, normalResponseMapper.toDto(null));

    }
}