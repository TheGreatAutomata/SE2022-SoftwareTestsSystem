package com.micro.contractserver.mapper;

import com.micro.contractserver.model.PerformanceTermPartyB;

import com.micro.dto.PerformanceTermPartyBDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface PerformanceTermPartyBMapper {

    public Collection<PerformanceTermPartyBDto> toDtos(Collection<PerformanceTermPartyB> performanceTermPartyBs);

    public Collection<PerformanceTermPartyB> toObjs(Collection<PerformanceTermPartyBDto> performanceTermPartyBDtos);

    public PerformanceTermPartyB toObj(PerformanceTermPartyBDto performanceTermPartyBDto);

    public PerformanceTermPartyBDto toDto(PerformanceTermPartyB performanceTermPartyB);

}
