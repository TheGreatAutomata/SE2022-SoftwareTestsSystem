package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyAResponse;

import com.micro.dto.PerformanceTermPartyAResponseDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface PerformanceTermPartyAResponseMapper {

    public Collection<PerformanceTermPartyAResponseDto> toDtos(Collection<PerformanceTermPartyAResponse> performanceTermPartyAResponses);

    public Collection<PerformanceTermPartyAResponse> toObjs(Collection<PerformanceTermPartyAResponseDto> performanceTermPartyAResponseDtos);

    public PerformanceTermPartyAResponse toObj(PerformanceTermPartyAResponseDto performanceTermPartyAResponseDto);

    public PerformanceTermPartyAResponseDto toDto(PerformanceTermPartyAResponse performanceTermPartyAResponse);

}