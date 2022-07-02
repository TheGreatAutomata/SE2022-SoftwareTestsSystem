package com.micro.testserver.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.DocModifyRecordDto;
import com.micro.dto.TestProgressTableDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

//测试方案
@Data
public class SoftwareTestScheme implements Serializable {
    public String name;

    public String 版本号;

    public List<DocModifyRecord> 文档修改记录 = null;

    public String _11标识;

    public String _12系统概述;

    public String _13文档概述;

    public String _14基线;

    public String _31硬件;

    public String _32软件;

    public String _33其他;

    public String _34参与组织;

    public String _35人员;

    public String _41总体设计;

    public String _411测试级别;

    public String _412测试类别;

    public String _413一般测试条件;

    public String _42计划执行的测试;

    public String _43测试用例;

    public TestProgressTable 测试进度表;

    public String 需求的可追踪性;
}
