package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.RuntimeEnvironment;
import com.micro.delegationserver.model.SoftwareChildFunction;
import com.micro.dto.RuntimeEnvironmentDto;
import com.micro.dto.SoftwareChildFunctionDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareChildFunctionMapper {
    public Collection<SoftwareChildFunctionDto> toDtos(Collection<SoftwareChildFunction> softwareChildFunctions);

    public Collection<SoftwareChildFunction> toObjs(Collection<SoftwareChildFunctionDto> softwareChildFunctionDtos);

    public SoftwareChildFunction toObj(SoftwareChildFunctionDto softwareChildFunctionDto);

    public SoftwareChildFunctionDto toDto(SoftwareChildFunction softwareChildFunction);
}
