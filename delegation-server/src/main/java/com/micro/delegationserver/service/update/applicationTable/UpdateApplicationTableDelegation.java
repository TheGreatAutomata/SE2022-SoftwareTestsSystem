package com.micro.delegationserver.service.update.applicationTable;

import com.micro.dto.DelegationApplicationTableDto;

public interface UpdateApplicationTableDelegation {
    public UpdateApplicationTableResult updateTable(String id, DelegationApplicationTableDto delegationApplicationTableDto);
}
