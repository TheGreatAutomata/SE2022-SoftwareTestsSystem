package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.*;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
public class SoftwareTestReport implements Serializable {
    public String 报告编号;
    public String 软件名称; // Test-SoftwareDocEvaluationTable
    public String 版本号; // Test-SoftwareDocEvaluationTable
    public String 总委托单位; // ? ? ? Contract/contractTable/contractTableExist/partyAName2
    public String 测试类别;
    public String 报告日期;
    public String 委托单位; // Contract/contractTable/contractTableExist/partyAName2
    public String 样品名称; // same as 软件名称
    public String 版本型号;
    public String 来样日期; // from sample
    public String 测试类型; // Delegation/application/Table, translate list to String
    public String 测试开始时间;
    public String 测试结束时间;
    public String 样品状态; // from sample
    public String 总测试依据; // Delegation/application/Table, merge to String
    public String 样品清单; // from sample
    public String 测试结论;
    public String 主测人;
    public String 主测人日期;
    public String 审核人;
    public String 审核人日期;
    public String 批准人;
    public String 批准人日期;
    public String 电话; // Contract/contractTable/contractTablePartyA/
    public String 传真; // Contract/contractTable/contractTablePartyA/
    public String 地址; // Contract/contractTable/contractTablePartyA/
    public String 邮编; // Contract/contractTable/contractTablePartyA/
    public String 联系人; // Contract/contractTable/contractTablePartyA/
    public String eMail;
    public List<TestReportHardwareEnv> 硬件环境 = null; // ? ? ?
    public List<TestReportSoftwareEnv> 软件环境 = null; // ? ? ?
    public String 网络环境; // Delegation/application/Table
    public List<TestReportTestDependency> 测试依据 = null; // // Delegation/application/Table
    public List<TestReportReference> 参考资料 = null;
    public List<TestReportFuncTest> 功能性测试 = null;
    public List<TestReportEfficiencyTest> 效率测试 = null;
    public List<TestReportPortabilityTest> 可移植性测试 = null;
    public List<TestReportUsabilityTest> 易用性测试 = null;
    public List<TestReportReliabilityTest> 可靠性测试 = null;
    public List<TestReportMaintainabilityTest> 可维护性测试 = null;
    public String 测试执行记录;
}
