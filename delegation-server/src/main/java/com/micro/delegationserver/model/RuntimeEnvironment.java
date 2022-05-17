package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.ClientRuntimeEnvironmentDto;
import com.micro.dto.ServerRuntimeEnvironmentDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class RuntimeEnvironment implements Serializable {
    public ClientRuntimeEnvironment 客户端;
    public ServerRuntimeEnvironment 服务器端;
    public String 网络环境;
}
