package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyA;

import com.micro.dto.PerformanceTermPartyADto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface PerformanceTermPartyAMapper {

    public Collection<PerformanceTermPartyADto> toDtos(Collection<PerformanceTermPartyA> performanceTermPartyAs);

    public Collection<PerformanceTermPartyA> toObjs(Collection<PerformanceTermPartyADto> performanceTermPartyADtos);

    public PerformanceTermPartyA toObj(PerformanceTermPartyADto performanceTermPartyADto);

    public PerformanceTermPartyADto toDto(PerformanceTermPartyA performanceTermPartyA);

}