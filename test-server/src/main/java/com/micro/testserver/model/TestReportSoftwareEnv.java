package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestReportSoftwareEnv {
    public String 软件类别;

    public String 软件名称;

    public String 版本;
}
