package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
public class ClientRuntimeEnvironment implements Serializable {
    public String 操作系统windows版本;
    public List<String> 操作系统 = null;
    public String 内存要求;
    public String 其他要求;
}
