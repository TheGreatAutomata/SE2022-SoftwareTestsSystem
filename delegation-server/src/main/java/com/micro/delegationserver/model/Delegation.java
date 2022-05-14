package com.micro.delegationserver.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "delegations")
public class Delegation implements Serializable {
    @GeneratedValue
    @Id
    public Long id;

    @NotNull
    public String usrId;

    public String usrName;

    public String delegationName;


    public Delegation(){

    }

    public Delegation(String usrId,String usrName,String delegationName){
        this.usrId=usrId;
        this.usrName=usrName;
        this.delegationName=delegationName;
    }
}
