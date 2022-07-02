package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContractTable implements Serializable {

    public ContractTableExist contractTableExist;
    public ContractTablePartyA contractTablePartyA;
    public ContractTablePartyB contractTablePartyB;

    public ContractTable() {
    }

    public ContractTable(ContractTableExist contractTableExist, ContractTablePartyA contractTablePartyA, ContractTablePartyB contractTablePartyB) {
        this.contractTableExist = contractTableExist;
        this.contractTablePartyA = contractTablePartyA;
        this.contractTablePartyB = contractTablePartyB;
    }
}
