package com.micro.delegationserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document("delegation")
@Data
public class Delegation implements Serializable {

    @Id
    public String id;
    @NotNull
    public String usrBelonged;
    public String usrName;

    public DelegationApplicationTable applicationTable;

    public DelegationState state;

    public String suggestion;

    public Delegation(){

    }

    public Delegation(String usrId,String usrName,DelegationApplicationTable applicationTable,DelegationState state){
        this.usrBelonged =usrId;
        this.usrName=usrName;
        this.applicationTable=applicationTable;
        this.state=state;
    }
}
