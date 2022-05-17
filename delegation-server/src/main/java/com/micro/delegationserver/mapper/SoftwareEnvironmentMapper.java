package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.SoftwareEnvironment;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.SoftwareEnvironmentDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareEnvironmentMapper {
    public Collection<SoftwareEnvironmentDto> toDtos(Collection<SoftwareEnvironment> softwareEnvironments);

    public Collection<SoftwareEnvironment> toObjs(Collection<SoftwareEnvironmentDto> softwareEnvironmentDtos);

    public SoftwareEnvironment toObj(SoftwareEnvironmentDto softwareEnvironmentDto);

    public SoftwareEnvironmentDto toDto(SoftwareEnvironment softwareEnvironment);
}
