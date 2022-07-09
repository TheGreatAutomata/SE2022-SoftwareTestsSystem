package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.SoftwareChildFunctionDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SoftwareFunctionProject implements Serializable {
    public List<SoftwareChildFunction> 子功能项目列表 = new ArrayList<>();

    public String 软件功能项目;
}
