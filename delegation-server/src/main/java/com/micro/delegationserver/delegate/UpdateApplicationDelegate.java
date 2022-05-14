package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.dao.CreatDelegationRequestDao;
import com.micro.delegationserver.model.dao.CreatDelegationRequestDatabaseObj;
import com.micro.delegationserver.repository.DelegationApplicationRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateApplicationDelegate implements JavaDelegate {

    @Autowired
    DelegationApplicationRepository delegationApplicationRepository;

    @Autowired
    CreatDelegationRequestDao creatDelegationRequestDao;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Update the application.");
        CreatDelegationRequest request=(CreatDelegationRequest) delegateExecution.getVariable("request");
        Long id=Long.valueOf((String) delegateExecution.getVariable("applicationId"));
        creatDelegationRequestDao.Update(request,id);
    }
}
