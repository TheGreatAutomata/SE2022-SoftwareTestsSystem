package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.DelegationAuditTestMaterialCheckDto;

import javax.validation.Valid;
import java.util.List;

public class DelegationAuditTestResult {
    private String 密级;

    private List<String> 查杀病毒 = null;

    private DelegationAuditTestMaterialCheck 材料检查;

    private String 确认意见;
}
