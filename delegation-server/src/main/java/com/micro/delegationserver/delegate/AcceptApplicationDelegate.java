package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.DelegationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class AcceptApplicationDelegate implements JavaDelegate {
    @Autowired
    DelegationRepository delegationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the delegation.");
        Delegation delegation=(Delegation) delegateExecution.getVariable("delegation");
        delegationRepository.save(delegation);
    }
}
