package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class SaveOfferConfirmationDelegate implements JavaDelegate {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Delegation delegation = (Delegation) delegateExecution.getVariable("delegation");
        String delegationId = delegation.getDelegationId();
        //delegation.getOfferTableUnion().set用户反馈(null);
        String usrState = delegation.getOfferTableUnion().get态度();
        delegateExecution.setVariable("delegationId",delegationId);

        switch (usrState)
        {
            case "接受":
            {
                delegateExecution.setVariable("state", 1);
                delegation.setState(DelegationState.TEST_MARKET_APPLICATION);
                break;
            }
            case "请求议价":
            {
                delegateExecution.setVariable("state", 2);
                delegation.setState(DelegationState.QUOTATION_USER_APPLICATION);
                break;
            }
            case "不接受":
            {
                delegateExecution.setVariable("state", 3);
                delegation.setState(DelegationState.QUOTATION_USER_DENIED);
                break;
            }
        }
        mongoTemplate.save(delegation,"delegation");
        delegateExecution.setVariable("delegationId", delegationId);
    }
}
