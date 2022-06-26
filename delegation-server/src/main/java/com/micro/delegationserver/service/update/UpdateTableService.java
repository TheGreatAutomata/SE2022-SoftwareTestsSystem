package com.micro.delegationserver.service.update;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.update.applicationTable.*;
import com.micro.delegationserver.service.update.functionTable.UpdateFunctionTableDelegation;
import com.micro.delegationserver.service.update.functionTable.UpdateFunctionTableDelegation_AFTER_APPLY;
import com.micro.delegationserver.service.update.functionTable.UpdateFunctionTableDelegation_UPLOAD_FILES;
import com.micro.delegationserver.service.update.functionTable.UpdateFunctionTableResult;
import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFunctionTableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UpdateTableService {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    UpdateApplicationTableDelegation_UPLOAD_FUNCTION_TABLE function_table;

    @Autowired
    UpdateApplicationTableDelegation_AFTER_APPLY after_apply;

    @Autowired
    UpdateFunctionTableDelegation_UPLOAD_FILES upload_files;

    @Autowired
    UpdateFunctionTableDelegation_AFTER_APPLY func_after_apply;

    public UpdateApplicationTableDelegation dispatchApplication(DelegationState state){
        switch (state){
            case ERROR:
                break;
            case UPLOAD_FUNCTION_TABLE:
            case UPLOAD_FILES:
            case AUDIT_TEST_APARTMENT:
            case AUDIT_TEST_APARTMENT_DENIED:
            case AUDIT_MARKET_APARTMENT_DENIED:
                return function_table;
            case AUDIT_MARKET_APARTMENT:
            case AUDIT_MARKET_APARTMENT_FURTHER:
            case QUOTATION_MARKET:
            case QUOTATION_USER:
            case QUOTATION_USER_DENIED:
            case QUOTATION_USER_APPLICATION:
            case TEST_MARKET_APPLICATION:
            case TEST_MARKET_CONTRACT:
                return after_apply;
            case IN_REVIEW:
                break;
            case DENIED:
                break;
            case ACCEPTED:
                break;
            case COMPLETED:
                break;
        }
        return null;
    }

    public UpdateFunctionTableDelegation dispatchFunction(DelegationState state){
        switch (state){
            case ERROR:
                break;
            case UPLOAD_FUNCTION_TABLE:
                break;
            case UPLOAD_FILES:
            case AUDIT_TEST_APARTMENT:
            case AUDIT_TEST_APARTMENT_DENIED:
            case AUDIT_MARKET_APARTMENT_DENIED:
                return upload_files;
            case AUDIT_MARKET_APARTMENT:
            case AUDIT_MARKET_APARTMENT_FURTHER:
            case QUOTATION_MARKET:
            case QUOTATION_USER:
            case QUOTATION_USER_DENIED:
            case QUOTATION_USER_APPLICATION:
            case TEST_MARKET_APPLICATION:
            case TEST_MARKET_CONTRACT:
                return func_after_apply;
            case IN_REVIEW:
                break;
            case DENIED:
                break;
            case ACCEPTED:
                break;
            case COMPLETED:
                break;
        }
        return null;
    }

    public UpdateApplicationTableResult updateApplicationTable(String id, DelegationApplicationTableDto delegationApplicationTableDto){
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            UpdateApplicationTableDelegation delegate= dispatchApplication(delegation.getState());
            if(delegate!=null) {
                return delegate.updateTable(id, delegationApplicationTableDto);
            }
        }
        return new UpdateApplicationTableResult(false);
    }

    public UpdateFunctionTableResult updateFunctionTable(String id, DelegationFunctionTableDto delegationFunctionTableDto){
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            UpdateFunctionTableDelegation delegate= dispatchFunction(delegation.getState());
            if(delegate!=null) {
                return delegate.updateTable(id, delegationFunctionTableDto);
            }
        }
        return new UpdateFunctionTableResult(false);
    }

}
