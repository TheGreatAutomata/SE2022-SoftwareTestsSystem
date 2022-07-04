package com.micro.sampleserver.mapper;

import com.micro.commonserver.model.SampleAcceptModel;
import com.micro.dto.SampleAcceptModelDto;
import org.mapstruct.Mapper;

@Mapper
public interface SampleAcceptModelMapper {
    public SampleAcceptModel toObj(SampleAcceptModelDto sampleMessageApplicationRequestDto);

    public SampleAcceptModelDto toDto(SampleAcceptModel sampleMessage);
}
