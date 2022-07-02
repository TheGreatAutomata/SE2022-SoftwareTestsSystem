package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.DelegationAuditTestMaterialCheckDto;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class DelegationAuditTestResult implements Serializable {
    public String 密级;

    public String 查杀工具;
    
    public String 查杀病毒 = null;

    public DelegationAuditTestMaterialCheck 材料检查;

    public String 确认意见;
}
