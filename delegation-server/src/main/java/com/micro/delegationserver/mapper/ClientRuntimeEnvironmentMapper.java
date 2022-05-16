package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.ClientRuntimeEnvironment;
import com.micro.delegationserver.model.RuntimeEnvironment;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.ClientRuntimeEnvironmentDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ClientRuntimeEnvironmentMapper {
    public Collection<ClientRuntimeEnvironmentDto> toDtos(Collection<ClientRuntimeEnvironment> clientRuntimeEnvironments);

    public Collection<ClientRuntimeEnvironment> toObjs(Collection<ClientRuntimeEnvironmentDto> clientRuntimeEnvironmentDtos);

    public ClientRuntimeEnvironment toObj(ClientRuntimeEnvironmentDto clientRuntimeEnvironmentDto);

    public ClientRuntimeEnvironmentDto toDto(ClientRuntimeEnvironment clientRuntimeEnvironment);
}
