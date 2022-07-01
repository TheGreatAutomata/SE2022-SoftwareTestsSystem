package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.TestProgressEntryDto;
import lombok.Data;

@Data
public class TestProgressTable {
    public TestProgressEntry 制定测试计划;

    public TestProgressEntry 设计测试;

    public TestProgressEntry 执行测试;

    public TestProgressEntry 评估测试;
}
