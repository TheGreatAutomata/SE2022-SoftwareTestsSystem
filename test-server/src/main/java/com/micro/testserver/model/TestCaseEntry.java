package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestCaseEntry implements Serializable {
    public String 测试分类;

    public String ID;

    public String 测试用例设计说明;

    public String 与本测试用例有关的规约说明;

    public String 预期的结果;

    public String 测试用例设计者;

    public String 测试时间;
}
