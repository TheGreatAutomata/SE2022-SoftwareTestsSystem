package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.dto.DelegationApplicationTableDto;
import org.mapstruct.Mapper;


import java.util.Collection;

@Mapper
public interface DelegationApplicationTableMapper {
    public Collection<DelegationApplicationTableDto> toDelegationApplicationTableDto(Collection<DelegationApplicationTable> delegationApplicationTables);

    public Collection<DelegationApplicationTable> toDelegationApplicationTables(Collection<DelegationApplicationTableDto> delegationApplicationTableDtos);
    public DelegationApplicationTable toDelegationApplicationTable(DelegationApplicationTableDto delegationApplicationTableDto);

    public DelegationApplicationTableDto toDelegationApplicationTableDto(DelegationApplicationTable delegationApplicationTable);
}
