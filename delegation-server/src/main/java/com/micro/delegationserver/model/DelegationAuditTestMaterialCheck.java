package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class DelegationAuditTestMaterialCheck implements Serializable {
    public List<String> 材料样品 = null;

    public List<String> 需求文档 = null;

    public List<String> 用户文档 = null;

    public List<String> 操作文档 = null;
}
