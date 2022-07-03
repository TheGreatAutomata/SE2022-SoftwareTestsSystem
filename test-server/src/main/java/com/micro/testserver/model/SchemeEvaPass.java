package com.micro.testserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SchemeEvaPass implements Serializable {
    public Boolean pass;
    public String failReason;
}
