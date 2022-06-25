package com.micro.delegationserver.service.update.functionTable;

import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFunctionTableDto;

public interface UpdateFunctionTableDelegation {
    public UpdateFunctionTableResult updateTable(String id, DelegationFunctionTableDto delegationFunctionTableDto);
}
