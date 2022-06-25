package com.micro.delegationserver.service.update.functionTable;

import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFunctionTableDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
//此时委托已经进入不能修改申请表的阶段
public class UpdateFunctionTable_CANNOT_MODIFY implements UpdateFunctionTableDelegation {
    @Override
    public UpdateFunctionTableResult updateTable(String id, DelegationFunctionTableDto delegationFunctionTableDto) {
        UpdateFunctionTableResult result=new UpdateFunctionTableResult();
        result.setHttpStatus(HttpStatus.valueOf(403));
        return result;
    }
}
