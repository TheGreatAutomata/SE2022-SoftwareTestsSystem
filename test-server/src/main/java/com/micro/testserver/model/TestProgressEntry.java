package com.micro.testserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestProgressEntry implements Serializable {
    public String 工作量;

    public String 开始时间;

    public String 结束时间;
}
