package com.micro.delegationserver.service.update.applicationTable;

import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.dto.DelegationApplicationTableDto;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

///还没有上传功能表，此时要修改就直接改数据库
@Component
public class UpdateApplicationTableDelegation_UPLOAD_FUNCTION_TABLE implements UpdateApplicationTableDelegation {


    @Autowired
    DelegationRepository delegationRepository;


    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;


    @Override
    public UpdateApplicationTableResult updateTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {
        System.out.println("Updating...uploadfunctiontable");

        UpdateApplicationTableResult updateApplicationTableResult=new UpdateApplicationTableResult();

        System.out.println(delegationApplicationTableDto.get版本号());
        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(delegationApplicationTableMapper.toDelegationApplicationTable(delegationApplicationTableDto));
            delegationRepository.save(delegation);
            updateApplicationTableResult.setHttpStatus(HttpStatus.OK);
        }else{
            updateApplicationTableResult.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return updateApplicationTableResult;
    }
}
