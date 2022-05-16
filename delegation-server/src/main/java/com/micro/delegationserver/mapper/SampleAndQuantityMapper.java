package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.SampleAndQuantity;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.SampleAndQuantityDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SampleAndQuantityMapper {
    public Collection<SampleAndQuantityDto> toDtos(Collection<SampleAndQuantity> sampleAndQuantities);

    public Collection<SampleAndQuantity> toObjs(Collection<SampleAndQuantityDto> sampleAndQuantityDtos);

    public SampleAndQuantity toObj(SampleAndQuantityDto sampleAndQuantityDto);

    public SampleAndQuantityDto toDto(SampleAndQuantity sampleAndQuantity);
}
