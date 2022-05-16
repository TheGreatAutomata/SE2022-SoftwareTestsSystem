package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.dto.DelegationFunctionTableDto;
import com.micro.dto.DelegationItemDto;
import org.mapstruct.Mapper;

import java.util.Collection;


public interface DelegationItemMapper {
    public Collection<DelegationItemDto> toDtos(Collection<Delegation> delegations);

    public Collection<Delegation> toObjs(Collection<DelegationItemDto> delegationItemDtos);

    public Delegation toObj(DelegationItemDto delegationItemDto);

    public DelegationItemDto toDto(Delegation delegation);
}
