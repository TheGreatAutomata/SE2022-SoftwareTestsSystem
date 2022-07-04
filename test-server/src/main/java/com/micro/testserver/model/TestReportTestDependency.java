package com.micro.testserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestReportTestDependency implements Serializable {
    public String 测试依据分项;
}
