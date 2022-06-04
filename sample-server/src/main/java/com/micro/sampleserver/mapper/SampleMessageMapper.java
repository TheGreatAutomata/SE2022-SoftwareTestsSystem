package com.micro.sampleserver.mapper;

import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.model.SampleMessage;
import org.mapstruct.Mapper;

@Mapper
public interface SampleMessageMapper {
    public SampleMessage toObj(SampleMessageApplicationRequestDto sampleMessageApplicationRequestDto);

    public SampleMessageApplicationRequestDto toDto(SampleMessage sampleMessage);
}
