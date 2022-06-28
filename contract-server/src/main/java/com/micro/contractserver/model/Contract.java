package com.micro.contractserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document("Contract")
@Data
public class Contract implements Serializable {

    @MongoId
    public String contractId;

    @NotNull
    public String usrId;

    public String usrName;

    public String delegationId;

    public ContractTable contractTable;

    public NondisclosureAgreementTable nondisclosureAgreementTable;

    public ContractState contractState;

    public String performanceTermState;
    public String performanceTermSuggestion;

    public String projectId;

    public Contract(String usrId, String usrName, String delegationId, ContractTable contractTable, NondisclosureAgreementTable nondisclosureAgreementTable, ContractState contractState) {
        this.usrId = usrId;
        this.usrName = usrName;
        this.delegationId = delegationId;
        this.contractTable = contractTable;
        this.nondisclosureAgreementTable = nondisclosureAgreementTable;
        this.contractState = contractState;
    }

}
