package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.SoftwareScale;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.SoftwareScaleDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareScaleMapper {
    public Collection<SoftwareScaleDto> toDtos(Collection<SoftwareScale> softwareScales);

    public Collection<SoftwareScale> toObjs(Collection<SoftwareScaleDto> softwareScaleDtos);

    public SoftwareScale toObj(SoftwareScaleDto softwareScaleDto);

    public SoftwareScaleDto toDto(SoftwareScale softwareScale);
}
