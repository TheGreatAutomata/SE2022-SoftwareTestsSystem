package com.micro.contractserver.mapper;

import com.micro.contractserver.model.Contract;
import com.micro.dto.ContractDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ContractMapper {
    
    public Collection<ContractDto> toDtos(Collection<Contract> contracts);

    public Collection<Contract> toObjs(Collection<ContractDto> contractDtos);

    public Contract toObj(ContractDto contractDto);

    public ContractDto toDto(Contract contract);
    
}
