package com.micro.delegationserver.model.dao;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.DelegationFunctionTable;
import com.micro.delegationserver.repository.DelegationApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        Optional<CreatDelegationRequestDatabaseObj> testObj = delegationApplicationRepository.findById(id);
        if(testObj.isPresent()){
            CreatDelegationRequestDatabaseObj dbObj=testObj.get();

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
        return null;
    }
    public CreatDelegationRequestDatabaseObj Update(CreatDelegationRequest creatDelegationRequest,Long id){
        CreatDelegationRequestDatabaseObj dbObj=new CreatDelegationRequestDatabaseObj(creatDelegationRequest);
        dbObj.id=id;
        delegationApplicationRepository.save(dbObj);
        return dbObj;
    }

}
