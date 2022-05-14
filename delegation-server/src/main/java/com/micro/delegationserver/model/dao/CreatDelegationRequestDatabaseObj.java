package com.micro.delegationserver.model.dao;

import com.micro.delegationserver.model.CreatDelegationRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delegation_applications")
public class CreatDelegationRequestDatabaseObj {
    @GeneratedValue
    @Id
    public Long id;
    public String applicationName;
    public String functionName;
    public String usrId;
    public String usrName;
    public CreatDelegationRequestDatabaseObj(){

    }

    public CreatDelegationRequestDatabaseObj(CreatDelegationRequest request){
        this.applicationName=request.getApplicationTable().getName();
        this.functionName=request.getFunctionTable().getName();
        this.usrId=request.getUsrId();
        this.usrName= request.getUsrName();
    }
}
