package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NondisclosureAgreementTable implements Serializable {

    public String partyAName;
    public String partyBName;
    public String projectName;

    public NondisclosureAgreementTable(String partyAName, String partyBName, String projectName) {
        this.partyAName = partyAName;
        this.partyBName = partyBName;
        this.projectName = projectName;
    }
}
