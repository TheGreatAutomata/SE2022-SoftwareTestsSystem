package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
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
    MongoDBDelegationRepository delegationRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Update the application.");
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        delegation.setState(DelegationState.AUDIT_TEST_APARTMENT);
        String delegationId = delegation.getDelegationId();
        Task task = taskService.createTaskQuery().processDefinitionKey("delegation_apply").processVariableValueEquals("delegationId",delegationId).singleResult();
        if(task != null)
        {
            runtimeService.deleteProcessInstance(task.getExecutionId(),"delegation has been deleted for modify");
        }
        mongoTemplate.save(delegation,"delegation");
    }
}
