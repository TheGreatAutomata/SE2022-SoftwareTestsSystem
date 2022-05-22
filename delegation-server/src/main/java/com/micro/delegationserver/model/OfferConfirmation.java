package com.micro.delegationserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OfferConfirmation implements Serializable {
    public String 委托人签字;
    public String 日期;
}
