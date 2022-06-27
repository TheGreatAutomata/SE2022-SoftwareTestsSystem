package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTable;
import com.micro.dto.ContractTableDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ContractTableMapper {

    public Collection<ContractTableDto> toDtos(Collection<ContractTable> contractTables);

    public Collection<ContractTable> toObjs(Collection<ContractTableDto> contractTableDtos);

    public ContractTable toObj(ContractTableDto contractTableDto);

    public ContractTableDto toDto(ContractTable contractTable);

}
