package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestReportUsabilityTest implements Serializable {
    public String 测试特性;

    public String 测试说明;

    public String 测试结果;
}
