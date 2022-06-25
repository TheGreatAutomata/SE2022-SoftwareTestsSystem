package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;


public class SaveApplicationDelegate implements JavaDelegate {

    @Autowired
    DelegationRepository delegationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the application.");
        Delegation currentDelegation=(Delegation) delegateExecution.getVariable("delegation");
        delegationRepository.save(currentDelegation);

        System.out.println(currentDelegation.delegationId);

        delegateExecution.setVariable("delegationId",currentDelegation.delegationId);
        delegateExecution.setVariable("delegation",currentDelegation);
        delegateExecution.setVariable("sampleMethod",currentDelegation.applicationTable.get样品和数量().get软件介质());
    }
}
