package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.HardwareEnvironmentDto;
import com.micro.dto.SoftwareEnvironmentDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServerRuntimeEnvironment implements Serializable {
    public HardwareEnvironment 硬件;
    public SoftwareEnvironment 软件;
}
