package com.micro.delegationserver.mapper;

import com.micro.commonserver.model.SampleAcceptItem;
import com.micro.delegationserver.model.RuntimeEnvironment;
import com.micro.delegationserver.model.SampleAndQuantity;
import com.micro.dto.RuntimeEnvironmentDto;
import com.micro.dto.SampleAcceptItemDto;
import com.micro.dto.SampleAndQuantityDto;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SampleAcceptItemMapper {
    public Collection<SampleAcceptItemDto> toDtos(Collection<SampleAcceptItem> runtimeEnvironments);

    public Collection<SampleAcceptItem> toObjs(Collection<SampleAcceptItemDto> runtimeEnvironmentDtos);
//    public List<SampleAcceptItemDto> toDtos(List<SampleAcceptItem> runtimeEnvironments);
//
//    public List<SampleAcceptItem> toObjs(List<SampleAcceptItemDto> runtimeEnvironmentDtos);

    public SampleAcceptItem toObj(SampleAcceptItemDto sampleAndQuantityDto);

    public SampleAcceptItemDto toDto(SampleAcceptItem sampleAndQuantity);
}
