package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.RuntimeEnvironment;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.RuntimeEnvironmentDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface RuntimeEnvironmentMapper {
    public Collection<RuntimeEnvironmentDto> toDtos(Collection<RuntimeEnvironment> runtimeEnvironments);

    public Collection<RuntimeEnvironment> toObjs(Collection<RuntimeEnvironmentDto> runtimeEnvironmentDtos);

    public RuntimeEnvironment toObj(RuntimeEnvironmentDto runtimeEnvironmentDto);

    public RuntimeEnvironmentDto toDto(RuntimeEnvironment runtimeEnvironment);
}
