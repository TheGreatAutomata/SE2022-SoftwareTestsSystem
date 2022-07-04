package com.micro.testserver.model;


import com.micro.dto.DocModifyRecordDto;
import com.micro.dto.TestProgressTableDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

//测试方案
@Data
public class SoftwareTestScheme implements Serializable {
    public String name;

    public String 版本号;

    public List<DocModifyRecord> 文档修改记录 = null;

    public String 标识;

    public String 系统概述;

    public String 文档概述;

    public String 基线;

    public String 硬件;

    public String 软件;

    public String 其他;

    public String 参与组织;

    public String 人员;

    public String 总体设计;

    public String 测试级别;

    public String 测试类别;

    public String 一般测试条件;

    public String 计划执行的测试;

    public String 测试用例;

    public TestProgressTable 测试进度表;

    public String 需求的可追踪性;
}
