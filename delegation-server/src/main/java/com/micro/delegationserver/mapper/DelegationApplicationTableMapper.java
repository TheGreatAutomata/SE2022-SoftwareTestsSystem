package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.DelegationFileList;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.dto.CreatDelegationRequestDto;
import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFileListDto;
import com.micro.dto.DelegationFunctionTableDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collection;

@Mapper
public interface DelegationApplicationTableMapper {
    public Collection<DelegationApplicationTableDto> toDelegationApplicationTableDto(Collection<DelegationApplicationTable> delegationApplicationTables);

    public Collection<DelegationApplicationTable> toDelegationApplicationTables(Collection<DelegationApplicationTableDto> delegationApplicationTableDtos);
    public DelegationApplicationTable toDelegationApplicationTable(DelegationApplicationTableDto delegationApplicationTableDto);

    public DelegationApplicationTableDto toDelegationApplicationTableDto(DelegationApplicationTable delegationApplicationTable);
}
