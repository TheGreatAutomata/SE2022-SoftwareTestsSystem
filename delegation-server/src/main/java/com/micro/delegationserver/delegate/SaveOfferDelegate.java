package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class SaveOfferDelegate  implements JavaDelegate {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        String delegationId = delegation.getDelegationId();
        delegation.getOfferTableUnion().set用户反馈(null);
        delegation.getOfferTableUnion().get基本信息().set小计(-1);
        delegation.getOfferTableUnion().get基本信息().set总计(-1);
        delegation.getOfferTableUnion().get基本信息().set税率8percent(-1);
        mongoTemplate.save(delegation,"delegation");
        delegateExecution.setVariable("delegationId", delegationId);
    }
}
