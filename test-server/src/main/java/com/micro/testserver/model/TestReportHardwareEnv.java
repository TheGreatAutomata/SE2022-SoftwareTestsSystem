package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestReportHardwareEnv {
    public String 硬件类别;

    public String 硬件名称;

    public String 配置;

    public String 数量;
}
