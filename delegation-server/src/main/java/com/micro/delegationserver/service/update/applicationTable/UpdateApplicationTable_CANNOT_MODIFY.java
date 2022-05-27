package com.micro.delegationserver.service.update.applicationTable;

import com.micro.dto.DelegationApplicationTableDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
//此时委托已经进入不能修改申请表的阶段
public class UpdateApplicationTable_CANNOT_MODIFY implements UpdateApplicationTableDelegation{
    @Override
    public UpdateApplicationTableResult updateTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {
        UpdateApplicationTableResult result=new UpdateApplicationTableResult();
        result.setHttpStatus(HttpStatus.valueOf(403));
        return result;
    }
}
