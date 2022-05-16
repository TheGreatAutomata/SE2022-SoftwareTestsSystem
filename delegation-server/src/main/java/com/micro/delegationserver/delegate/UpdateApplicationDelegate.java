package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateApplicationDelegate implements JavaDelegate {


    @Autowired
    MongoDBDelegationRepository delegationRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Update the application.");
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        delegation.setState(DelegationState.IN_REVIEW);
        delegationRepository.save(delegation);
    }
}
