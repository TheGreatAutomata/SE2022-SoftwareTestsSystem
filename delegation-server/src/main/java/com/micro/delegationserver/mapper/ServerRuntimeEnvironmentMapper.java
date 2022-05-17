package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.ServerRuntimeEnvironment;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.ServerRuntimeEnvironmentDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ServerRuntimeEnvironmentMapper {
    public Collection<ServerRuntimeEnvironmentDto> toDtos(Collection<ServerRuntimeEnvironment> serverRuntimeEnvironments);

    public Collection<ServerRuntimeEnvironment> toObjs(Collection<ServerRuntimeEnvironmentDto> serverRuntimeEnvironmentDtos);

    public ServerRuntimeEnvironment toObj(ServerRuntimeEnvironmentDto serverRuntimeEnvironmentDto);

    public ServerRuntimeEnvironmentDto toDto(ServerRuntimeEnvironment serverRuntimeEnvironment);
}
