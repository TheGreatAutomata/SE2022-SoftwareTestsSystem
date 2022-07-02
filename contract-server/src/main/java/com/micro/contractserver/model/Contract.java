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

    public String projectId;

    public ContractState contractState;

    public String performanceTermState;

    public String performanceTermSuggestion;

    public ContractTable contractTable;

    public NondisclosureAgreementTable nondisclosureAgreementTable;

    public minioFileItem signedContractTableFile;

    public minioFileItem signedNondisclosureAgreementTableFile;

    public Contract() {
    }

    public Contract(String delegationId, ContractTable contractTable) {
        this.delegationId = delegationId;
        this.contractTable = contractTable;
    }

}
