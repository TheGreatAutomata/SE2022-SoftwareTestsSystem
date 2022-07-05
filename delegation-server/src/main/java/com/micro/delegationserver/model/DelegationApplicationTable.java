package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.RuntimeEnvironmentDto;
import com.micro.dto.SampleAndQuantityDto;
import com.micro.dto.SoftwareScaleDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class DelegationApplicationTable implements Serializable {
    public String name;
    public String 测试类型其他;
    public List<String> 测试类型 = new ArrayList<>();
    public String 软件名称;
    public String 版本号;
    public String 委托单位中文;
    public String 委托单位英文;
    public String 开发单位;
    public String 单位性质;
    public String 软件用户对象描述;
    public String 主要功能及用途简介限300字;
    public List<String> 测试依据 = new ArrayList<>();
    public List<String> 需要测试的技术指标 = new ArrayList<>();
    public SoftwareScale 软件规模;
    public String 软件类型;
    public RuntimeEnvironment 运行环境;
    public SampleAndQuantity 样品和数量;
    public String 希望测试完成时间;
    public ClientEnterpriseInfo 委托单位信息;
}
