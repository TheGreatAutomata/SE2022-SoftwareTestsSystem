package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTablePartyA;
import com.micro.dto.ContractTablePartyADto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ContractTablePartyAMapper {

    public Collection<ContractTablePartyADto> toDtos(Collection<ContractTablePartyA> contractTablePartyAs);

    public Collection<ContractTablePartyA> toObjs(Collection<ContractTablePartyADto> contractTablePartyADtos);

    public ContractTablePartyA toObj(ContractTablePartyADto contractTablePartyADto);

    public ContractTablePartyADto toDto(ContractTablePartyA contractTablePartyA);

}