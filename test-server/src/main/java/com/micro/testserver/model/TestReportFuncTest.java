package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestReportFuncTest implements Serializable {
    public String 功能模块;

    public String 功能要求;

    public String 测试结果;
}
