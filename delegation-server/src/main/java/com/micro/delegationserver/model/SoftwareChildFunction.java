package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SoftwareChildFunction implements Serializable {
    public String 软件子功能项目;

    public String 功能说明;
}
