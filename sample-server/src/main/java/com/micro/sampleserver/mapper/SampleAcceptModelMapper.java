package com.micro.sampleserver.mapper;

import com.micro.dto.SampleAcceptDto;
import com.micro.commonserver.model.SampleAcceptModel;
import org.mapstruct.Mapper;

@Mapper
public interface SampleAcceptModelMapper {
    public SampleAcceptModel toObj(SampleAcceptDto sampleMessageApplicationRequestDto);

    public SampleAcceptDto toDto(SampleAcceptModel sampleMessage);
}
