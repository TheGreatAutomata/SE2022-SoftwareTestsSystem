package com.micro.delegationserver.mapper;

import com.micro.commonserver.model.SampleAcceptItem;
import com.micro.commonserver.model.SampleAcceptModel;
import com.micro.dto.SampleAcceptItemDto;
import com.micro.dto.SampleAcceptModelDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SampleAcceptModelMapper {
    public Collection<SampleAcceptModelDto> toDtos(Collection<SampleAcceptModel> runtimeEnvironments);
    public Collection<SampleAcceptModel> toObjs(Collection<SampleAcceptModelDto> runtimeEnvironmentDtos);
    public SampleAcceptModel toObj(SampleAcceptModelDto sampleMessageApplicationRequestDto);

    public SampleAcceptModelDto toDto(SampleAcceptModel sampleMessage);

//    public default SampleAcceptItem map(SampleAcceptItemDto value)
//    {
//        SampleAcceptItem r = new SampleAcceptItem();
//        r
//    }
}
