package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.DelegationFileList;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.dto.DelegationFileListDto;
import com.micro.dto.DelegationFunctionTableDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface DelegationFunctionTableMapper {
    public Collection<DelegationFunctionTableDto> toDtos(Collection<DelegationFunctionTable> functionTables);

    public Collection<DelegationFunctionTable> toObjs(Collection<DelegationFunctionTableDto> functionTableDtos);

    public DelegationFunctionTable toObj(DelegationFunctionTableDto functionTableDto);

    public DelegationFunctionTableDto toDto(DelegationFunctionTable functionTable);
}
