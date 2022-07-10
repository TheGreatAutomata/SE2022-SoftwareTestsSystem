package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class SaveComplete implements JavaDelegate {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 完成文件流程
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        mongoTemplate.save(delegation,"delegation");
    }
}
