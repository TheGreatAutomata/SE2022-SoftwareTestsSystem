package com.micro.sampleserver.mapper;

import com.micro.dto.SampleAcceptDto;
import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.model.SampleAcceptModel;
import com.micro.sampleserver.model.SampleMessage;
import org.mapstruct.Mapper;

@Mapper
public interface SampleAcceptModelMapper {
    public SampleAcceptModel toObj(SampleAcceptDto sampleMessageApplicationRequestDto);

    public SampleAcceptDto toDto(SampleAcceptModel sampleMessage);
}
