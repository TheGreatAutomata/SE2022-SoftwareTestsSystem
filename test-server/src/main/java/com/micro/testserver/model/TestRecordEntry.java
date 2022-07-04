package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestRecordEntry implements Serializable {
    public String 测试分类;
    public String 序号;

    public String 测试特性;
    public String 测试用例设计说明;

    public String 与本测试用例有关的规约说明;

    public String 前提条件;

    public String 测试用例执行过程;

    public String 预期的结果;

    public String 测试用例设计者;

    public String 实际结果;
    public String 是否与预期结果一致;

    public String 相关的bUG编号;

    public String 用例执行者;

    public String 执行测试时间;

    public String 确认人;
}
