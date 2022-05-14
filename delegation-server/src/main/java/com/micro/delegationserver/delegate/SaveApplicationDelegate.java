package com.micro.delegationserver.delegate;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.dao.CreatDelegationRequestDao;
import com.micro.delegationserver.model.dao.CreatDelegationRequestDatabaseObj;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class SaveApplicationDelegate implements JavaDelegate {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    CreatDelegationRequestDao creatDelegationRequestDao;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the application.");
        CreatDelegationRequest request=(CreatDelegationRequest) delegateExecution.getVariable("request");
        CreatDelegationRequestDatabaseObj dbobj = creatDelegationRequestDao.Save(request);
        delegateExecution.setVariable("applicationId",dbobj.id);
        System.out.println(dbobj.id);
    }
}
