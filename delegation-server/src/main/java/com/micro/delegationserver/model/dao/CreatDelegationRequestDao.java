package com.micro.delegationserver.model.dao;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.delegationserver.repository.DelegationApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatDelegationRequestDao {
    @Autowired
    DelegationApplicationRepository delegationApplicationRepository;

    public CreatDelegationRequestDatabaseObj Save(CreatDelegationRequest creatDelegationRequest){
        CreatDelegationRequestDatabaseObj dbObj=new CreatDelegationRequestDatabaseObj(creatDelegationRequest);
        delegationApplicationRepository.save(dbObj);
        return dbObj;
    }
    public CreatDelegationRequest Get(Long id){
        CreatDelegationRequestDatabaseObj dbObj=(CreatDelegationRequestDatabaseObj) delegationApplicationRepository.getById(id);
        CreatDelegationRequest request=new CreatDelegationRequest();
        request.setUsrName(dbObj.usrName);
        request.setUsrId(dbObj.usrId);
        DelegationApplicationTable applicationTable=new DelegationApplicationTable();
        applicationTable.setName(dbObj.applicationName);
        DelegationFunctionTable functionTable=new DelegationFunctionTable();
        functionTable.setName(dbObj.functionName);
        request.setApplicationTable(applicationTable);
        request.setFunctionTable(functionTable);
        return request;
    }
}
