package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerformanceTermPartyA implements Serializable {

    public String 态度;
    public String 意见;

    public PerformanceTermPartyA(String 态度, String 意见) {
        this.态度 = 态度;
        this.意见 = 意见;
    }
}
