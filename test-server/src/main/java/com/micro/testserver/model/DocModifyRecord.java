package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DocModifyRecord implements Serializable {
    public String 版本号;

    public String 日期;

    public String AMD;

    public String 修订者;

    public String 说明;
}
