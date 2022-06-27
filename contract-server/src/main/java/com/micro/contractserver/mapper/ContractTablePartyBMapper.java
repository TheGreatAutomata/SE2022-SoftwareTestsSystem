package com.micro.contractserver.mapper;

import com.micro.contractserver.model.ContractTablePartyB;
import com.micro.dto.ContractTablePartyBDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ContractTablePartyBMapper {

    public Collection<ContractTablePartyBDto> toDtos(Collection<ContractTablePartyB> contractTablePartyBs);

    public Collection<ContractTablePartyB> toObjs(Collection<ContractTablePartyBDto> contractTablePartyBDtos);

    public ContractTablePartyB toObj(ContractTablePartyBDto contractTablePartyBDto);

    public ContractTablePartyBDto toDto(ContractTablePartyB contractTablePartyB);

}