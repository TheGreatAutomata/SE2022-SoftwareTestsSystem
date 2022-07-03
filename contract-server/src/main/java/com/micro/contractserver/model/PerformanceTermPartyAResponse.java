package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerformanceTermPartyAResponse implements Serializable {

    public String contractId;
    public PerformanceTermPartyB 履行期限受托方部分;

    public PerformanceTermPartyAResponse() {
    }

    public PerformanceTermPartyAResponse(String contractId, PerformanceTermPartyB 履行期限受托方部分) {

        this.contractId = contractId;
        this.履行期限受托方部分 = 履行期限受托方部分;

    }

}
