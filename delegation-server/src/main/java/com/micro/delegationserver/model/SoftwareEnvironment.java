package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
public class SoftwareEnvironment implements Serializable {
    public String 操作系统;

    public String 版本;

    public String 编程语言;

    public List<String> 架构 = null;

    public String 数据库;

    public String 中间件;

    public String 其他支撑软件;
}
