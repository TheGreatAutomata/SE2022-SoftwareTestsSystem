package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestReportHardwareEnv implements Serializable {
    public String 硬件类别;

    public String 硬件名称;

    public String 配置;

    public String 数量;
}
