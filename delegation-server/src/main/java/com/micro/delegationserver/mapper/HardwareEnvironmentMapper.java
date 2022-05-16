package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.HardwareEnvironment;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.HardwareEnvironmentDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface HardwareEnvironmentMapper {
    public Collection<HardwareEnvironmentDto> toDtos(Collection<HardwareEnvironment> hardwareEnvironments);

    public Collection<HardwareEnvironment> toObjs(Collection<HardwareEnvironmentDto> hardwareEnvironmentDtos);

    public HardwareEnvironment toObj(HardwareEnvironmentDto hardwareEnvironmentDto);

    public HardwareEnvironmentDto toDto(HardwareEnvironment hardwareEnvironment);
}
