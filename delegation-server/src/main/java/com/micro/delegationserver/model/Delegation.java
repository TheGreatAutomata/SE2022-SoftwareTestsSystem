package com.micro.delegationserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document("delegation")
@Data
public class Delegation implements Serializable {

    @MongoId
    public String delegationId;
    @NotNull
    public String usrBelonged;
    public String usrName;

    public DelegationApplicationTable applicationTable;

    public DelegationFunctionTable functionTable;

    public DelegationState state;

    public String suggestion;

    public DelegationAuditTestResult auditTestResult;

    public String auditMarketResult;

    public String projectId;

    public OfferTableUnion offerTableUnion;

    public Delegation(){

    }

    public Delegation(String usrId,String usrName,DelegationApplicationTable applicationTable,DelegationState state){
        this.usrBelonged =usrId;
        this.usrName=usrName;
        this.applicationTable=applicationTable;
        this.state=state;
    }
}
