package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.SoftwareFunctionProjectDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DelegationFunctionTable implements Serializable {
    public String 软件名称;

    public String 版本号;

    public List<SoftwareFunctionProject> 功能项目列表 = new ArrayList<>();
}
