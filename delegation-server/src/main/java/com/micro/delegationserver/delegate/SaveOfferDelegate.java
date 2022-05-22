package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.ProjectOfferItem;
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
        //delegation.getOfferTableUnion().set用户反馈(null);
        int sum = 0;
        for(ProjectOfferItem i: delegation.getOfferTableUnion().get基本信息().get项目列表())
        {
            sum += i.行合计;
        }
        delegation.getOfferTableUnion().get基本信息().set小计(sum);
        delegation.getOfferTableUnion().get基本信息().set税率8percent((int)(sum*0.08));
        delegation.getOfferTableUnion().get基本信息().set总计((int)(sum*0.92));
        mongoTemplate.save(delegation,"delegation");
        delegateExecution.setVariable("delegationId", delegationId);
    }
}
