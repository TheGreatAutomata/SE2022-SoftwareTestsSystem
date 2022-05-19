package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

public class DelegationAuditTestMaterialCheck {
    private List<String> 材料样品 = null;

    private List<String> 需求文档 = null;

    private List<String> 用户文档 = null;

    private List<String> 操作文档 = null;
}
