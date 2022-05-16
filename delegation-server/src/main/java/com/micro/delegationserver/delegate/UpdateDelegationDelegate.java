package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Access;

public class UpdateDelegationDelegate implements JavaDelegate {

    @Autowired
    DelegationRepository delegationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Update the delegation.");
        Delegation delegation=(Delegation) delegateExecution.getVariable("delegation");
        delegation.id= Long.valueOf((String) delegateExecution.getVariable("applicationId"));
        delegationRepository.save(delegation);
    }
}
