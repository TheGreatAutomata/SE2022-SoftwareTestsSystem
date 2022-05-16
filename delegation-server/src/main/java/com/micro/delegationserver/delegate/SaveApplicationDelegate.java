package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;


public class SaveApplicationDelegate implements JavaDelegate {

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the application.");
        Delegation currentDelegation=(Delegation) delegateExecution.getVariable("delegation");
        currentDelegation=delegationRepository.save(currentDelegation);
        currentDelegation.setState(DelegationState.IN_REVIEW);
        delegationRepository.save(currentDelegation);

        System.out.println(currentDelegation.id);

        delegateExecution.setVariable("delegationId",currentDelegation.id);

        delegateExecution.setVariable("delegation",currentDelegation);
    }
}
