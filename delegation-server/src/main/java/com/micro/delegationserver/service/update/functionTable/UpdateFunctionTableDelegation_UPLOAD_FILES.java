package com.micro.delegationserver.service.update.functionTable;

import com.micro.delegationserver.mapper.DelegationFunctionTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.dto.DelegationFunctionTableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

///还没有上传功能表，此时要修改就直接改数据库
@Component
public class UpdateFunctionTableDelegation_UPLOAD_FILES implements UpdateFunctionTableDelegation {


    @Autowired
    DelegationRepository delegationRepository;


    @Autowired
    DelegationFunctionTableMapper delegationFunctionTableMapper;


    @Override
    public UpdateFunctionTableResult updateTable(String id, DelegationFunctionTableDto delegationFunctionTableDto) {
        System.out.println("Updating...uploadfunctiontable");

        UpdateFunctionTableResult updateApplicationTableResult=new UpdateFunctionTableResult();

        System.out.println(delegationFunctionTableDto.get版本号());
        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setFunctionTable(delegationFunctionTableMapper.toObj(delegationFunctionTableDto));
            delegationRepository.save(delegation);
            updateApplicationTableResult.setHttpStatus(HttpStatus.OK);
        }else{
            updateApplicationTableResult.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        return updateApplicationTableResult;
    }
}
