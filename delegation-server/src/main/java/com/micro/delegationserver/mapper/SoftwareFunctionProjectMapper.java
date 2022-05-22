package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.RuntimeEnvironment;
import com.micro.delegationserver.model.SoftwareFunctionProject;
import com.micro.dto.RuntimeEnvironmentDto;
import com.micro.dto.SoftwareFunctionProjectDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareFunctionProjectMapper {
    public Collection<SoftwareFunctionProjectDto> toDtos(Collection<SoftwareFunctionProject> softwareFunctionProjects);

    public Collection<SoftwareFunctionProject> toObjs(Collection<SoftwareFunctionProjectDto> softwareFunctionProjectDtos);

    public SoftwareFunctionProject toObj(SoftwareFunctionProjectDto softwareFunctionProjectDto);

    public SoftwareFunctionProjectDto toDto(SoftwareFunctionProject softwareFunctionProject);
}
