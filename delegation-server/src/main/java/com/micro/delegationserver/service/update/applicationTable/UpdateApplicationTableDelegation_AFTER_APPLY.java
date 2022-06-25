package com.micro.delegationserver.service.update.applicationTable;

import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.dto.DelegationApplicationTableDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

///此时已经提交了三张表并经过审核，要更新需要重新开启流程
@Component
public class UpdateApplicationTableDelegation_AFTER_APPLY implements UpdateApplicationTableDelegation{

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    @Autowired
    RuntimeService runtimeService;



    @Override
    public UpdateApplicationTableResult updateTable(String id, DelegationApplicationTableDto delegationApplicationTableDto) {
        System.out.println("Updating...");

        UpdateApplicationTableResult updateApplicationTableResult=new UpdateApplicationTableResult();

        Optional<Delegation> delegation_op=delegationRepository.findById(id);

        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setApplicationTable(delegationApplicationTableMapper.toDelegationApplicationTable(delegationApplicationTableDto));
            delegationRepository.save(delegation);
            delegation.setState(DelegationState.AUDIT_TEST_DPARTMENT);

            //删除当前的流程
            List<ProcessInstance> instances=runtimeService.createProcessInstanceQuery().processDefinitionKey("delegation_apply").variableValueEquals("delegationId",id).list();
            for (ProcessInstance instance:instances
                 ) {
                runtimeService.deleteProcessInstance(instance.getId(),"The application table of delegation whose id is "+id+" has been modified.");
            }

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("delegation", delegation);
            variables.put("delegationId",id);

            runtimeService.startProcessInstanceByKey("delegation_modify",variables);
            updateApplicationTableResult.setHttpStatus(HttpStatus.OK);



        }else{
            updateApplicationTableResult.setHttpStatus(HttpStatus.NOT_FOUND);
        }

        return updateApplicationTableResult;
    }
}
