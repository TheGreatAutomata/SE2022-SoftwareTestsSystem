package com.micro.delegationserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Document("delegation")
@Data
public class Delegation implements Serializable {

    @Id
    public String id;

    @NotNull
    public String usrId;

    public String usrName;
    public String delegationName;

    public DelegationState state;

    public Map<String,String> applicationTable;

    public Delegation(){

    }

    public Delegation(String usrId,String usrName,String delegationName){
        this.usrId=usrId;
        this.usrName=usrName;
        this.delegationName=delegationName;
    }
}
