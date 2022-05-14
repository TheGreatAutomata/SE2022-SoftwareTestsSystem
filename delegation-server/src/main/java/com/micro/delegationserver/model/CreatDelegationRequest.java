package com.micro.delegationserver.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
public class CreatDelegationRequest implements Serializable {
    DelegationApplicationTable applicationTable;
    DelegationFunctionTable functionTable;
    DelegationFileList fileList;
    String usrId;
    String usrName;
}
