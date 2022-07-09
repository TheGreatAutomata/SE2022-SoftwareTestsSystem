package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class HardwareEnvironment implements Serializable {
    public List<String> 架构 = new ArrayList<>();

    public String 内存要求;

    public String 硬盘要求;

    public String 其他要求;
}
