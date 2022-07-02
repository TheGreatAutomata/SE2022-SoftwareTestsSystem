package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestReportReliabilityTest {
    public String 测试特性;

    public String 测试说明;

    public String 测试结果;
}
