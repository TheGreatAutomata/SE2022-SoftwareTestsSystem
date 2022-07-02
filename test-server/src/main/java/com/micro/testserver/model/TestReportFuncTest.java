package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestReportFuncTest {
    public String 功能模块;

    public String 功能要求;

    public String 测试结果;
}
