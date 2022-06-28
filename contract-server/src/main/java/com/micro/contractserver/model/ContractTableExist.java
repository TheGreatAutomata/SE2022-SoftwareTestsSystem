package com.micro.contractserver.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class ContractTableExist implements Serializable {

    public String projectName;
    public String partyAName1;
    public String partyBName1;
    public String partyAName2;
    public String partyBName2;
    public String softwareName;
    public String softwareQualityCharacteristic;
    public String paymentInChinese;
    public String paymentInArabic;
    public String performanceTerm;
    public String rectificationTimes;
    public String rectificationTerm;
    public String partyAName3;
    public String partyBName3;
    public String partyBDepositBank;
    public String partyBBankAccountName;
    public String partyBBankAccountNumber;

    public ContractTableExist(String projectName, String partyBName1, String partyBName2, String partyBName3, String performanceTerm, String rectificationTimes, String rectificationTerm) {

        this.projectName = projectName;
        this.partyBName1 = partyBName1;
        this.partyBName2 = partyBName2;
        this.partyBName3 = partyBName3;
        this.performanceTerm = performanceTerm;
        this.rectificationTimes = rectificationTimes;
        this.rectificationTerm = rectificationTerm;

    }
}
