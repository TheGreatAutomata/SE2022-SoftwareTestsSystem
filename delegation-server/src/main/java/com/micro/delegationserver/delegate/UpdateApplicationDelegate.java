package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;

import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.model.DelegationState;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

public class UpdateApplicationDelegate implements JavaDelegate {


    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    MongoTemplate mongoTemplate;



    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Update the application.");
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
        String delegationId = delegation.getDelegationId();
        mongoTemplate.save(delegation,"delegation");
    }
}
