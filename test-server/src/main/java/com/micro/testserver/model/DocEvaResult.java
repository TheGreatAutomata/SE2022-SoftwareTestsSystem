package com.micro.testserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DocEvaResult implements Serializable {
    public String result;
    public String description;
}
