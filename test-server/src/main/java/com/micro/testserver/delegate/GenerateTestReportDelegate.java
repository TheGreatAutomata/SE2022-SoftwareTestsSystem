package com.micro.testserver.delegate;

import com.micro.commonserver.model.SampleAcceptModel;
import com.micro.contractserver.model.Contract;
import com.micro.delegationserver.model.Delegation;
import com.micro.testserver.model.*;
import com.micro.testserver.repository.ContractRepository;
import com.micro.testserver.repository.DelegationRepository;
import com.micro.testserver.repository.SampelAcceptModelRepository;
import com.micro.testserver.repository.SoftwareTestRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenerateTestReportDelegate implements JavaDelegate {

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    SampelAcceptModelRepository sampelAcceptModelRepository;

    String delim = "\n";


    @Override
    public void execute(DelegateExecution delegateExecution) {
        SoftwareTest softwareTest = delegateExecution.getVariable("softwareTest", SoftwareTest.class);
        if (softwareTest == null) {
            //？！
            return;
        }

        SoftwareTestReport report = new SoftwareTestReport();

        System.out.println(softwareTest.getDelegation_id());

        Optional<Delegation> delegationOptional = delegationRepository.findByDelegationId(softwareTest.getDelegation_id());

        //Get Delegation
        Delegation delegation = new Delegation();
        if (delegationOptional.isPresent())
            delegation = delegationOptional.get();

        //测试类型
        report.set测试类型(String.join(delim, delegation.getApplicationTable().get测试类型()));

        //总测试依据
        report.set总测试依据(String.join(delim, delegation.getApplicationTable().get测试依据()));

        //测试依据
        List<TestReportTestDependency> testReportTestDependencyList = new ArrayList<>();
        for(var v : delegation.getApplicationTable().get测试依据()){
            TestReportTestDependency testReportTestDependency = new TestReportTestDependency();
            testReportTestDependency.set测试依据分项(v);
            testReportTestDependencyList.add(testReportTestDependency);
        }
        report.set测试依据(testReportTestDependencyList);

        //网络环境
        report.set网络环境(delegation.getApplicationTable().运行环境.网络环境);

        //Get the Contract
        Contract contract  = contractRepository.findByDelegationId(softwareTest.getDelegation_id());
        if(contract==null){
            throw new NullPointerException();
        }

        //委托单位
        report.set委托单位(contract.getContractTable().getContractTableExist().getPartyAName2());

        //总委托单位
        report.set总委托单位(contract.getContractTable().getContractTableExist().getPartyAName2());

        //电话
        report.set电话(delegation.getApplicationTable().get委托单位信息().get电话());

        //传真
        report.set传真(delegation.getApplicationTable().get委托单位信息().get传真());

        //地址
        report.set地址(delegation.getApplicationTable().get委托单位信息().get地址());

        //邮编
        report.set邮编(delegation.getApplicationTable().get委托单位信息().get邮编());

        //联系人
        report.set联系人(delegation.getApplicationTable().get委托单位信息().get联系人());

        //Email
        report.setEmail(delegation.getApplicationTable().get委托单位信息().getEMail());

        // 测试单位单位地址
        report.set测试单位单位地址(contract.getContractTable().getContractTablePartyB().get通讯地址());

        // 测试单位邮政编码
        report.set测试单位邮政编码(contract.getContractTable().getContractTablePartyB().get邮编());

        // 测试单位电话
        report.set测试单位电话(contract.getContractTable().getContractTablePartyB().get电话());

        // 测试单位传真
        report.set测试单位传真(contract.getContractTable().getContractTablePartyB().get传真());

        //样品名称
        report.set样品名称(softwareTest.getDocEvaluationTable().get软件名称());

        //软件名称
        report.set软件名称(softwareTest.getDocEvaluationTable().get软件名称());

        //版本号
        report.set版本号(softwareTest.getDocEvaluationTable().get版本号());

        SoftwareTestRecord softwareTestRecord = softwareTest.getTestRecord();

        //软件测试记录
        for(var rec : softwareTestRecord.get软件测试记录()){
            if(rec.get测试分类().equals("功能性测试")){
                TestReportFuncTest testReportFuncTest = new TestReportFuncTest();
                testReportFuncTest.set功能模块(rec.get测试特性());
                testReportFuncTest.set功能要求(rec.get预期的结果());
                testReportFuncTest.set测试结果(rec.get是否与预期结果一致());
                report.get功能性测试().add(testReportFuncTest);
            }
            else{
                if(rec.get测试分类().equals("效率测试")) {
                    TestReportEfficiencyTest testReportEfficiencyTest = new TestReportEfficiencyTest();
                    testReportEfficiencyTest.set测试特性(rec.get测试特性());
                    testReportEfficiencyTest.set测试说明(rec.get预期的结果());
                    testReportEfficiencyTest.set测试结果(rec.get是否与预期结果一致());
                    report.get效率测试().add(testReportEfficiencyTest);
                }
                else if(rec.get测试分类().equals("可移植性测试")) {
                    TestReportPortabilityTest testReportPortabilityTest = new TestReportPortabilityTest();
                    testReportPortabilityTest.set测试特性(rec.get测试特性());
                    testReportPortabilityTest.set测试说明(rec.get预期的结果());
                    testReportPortabilityTest.set测试结果(rec.get是否与预期结果一致());
                    report.get可移植性测试().add(testReportPortabilityTest);
                }
                else if(rec.get测试分类().equals("易用性测试")) {
                    TestReportUsabilityTest testReportUsabilityTest = new TestReportUsabilityTest();
                    testReportUsabilityTest.set测试特性(rec.get测试特性());
                    testReportUsabilityTest.set测试说明(rec.get预期的结果());
                    testReportUsabilityTest.set测试结果(rec.get是否与预期结果一致());
                    report.get易用性测试().add(testReportUsabilityTest);
                }
                else if(rec.get测试分类().equals("可靠性测试")) {
                    TestReportReliabilityTest testReportReliabilityTest = new TestReportReliabilityTest();
                    testReportReliabilityTest.set测试特性(rec.get测试特性());
                    testReportReliabilityTest.set测试说明(rec.get预期的结果());
                    testReportReliabilityTest.set测试结果(rec.get是否与预期结果一致());
                    report.get可靠性测试().add(testReportReliabilityTest);
                }
                else if(rec.get测试分类().equals("可维护性测试")) {
                    TestReportMaintainabilityTest testReportMaintainabilityTest = new TestReportMaintainabilityTest();
                    testReportMaintainabilityTest.set测试特性(rec.get测试特性());
                    testReportMaintainabilityTest.set测试说明(rec.get预期的结果());
                    testReportMaintainabilityTest.set测试结果(rec.get是否与预期结果一致());
                    report.get可维护性测试().add(testReportMaintainabilityTest);
                }
            }
        }

        Optional<SampleAcceptModel> sampleAcceptModelOptional = sampelAcceptModelRepository.findById(softwareTest.getDelegation_id());
        SampleAcceptModel sampleAcceptModel = new SampleAcceptModel();
        if(sampleAcceptModelOptional.isPresent()) {
            sampleAcceptModel = sampleAcceptModelOptional.get();
        }

        //样品状态
        report.set样品状态(sampleAcceptModel.get样品状态());

        //样品清单
        report.set样品清单(String.join(delim,sampleAcceptModel.getString样品列表()));

        //来样日期
        report.set来样日期(sampleAcceptModel.get来样日期());

        softwareTest.setTestReport(report);
        System.out.println(softwareTestRepository);
        //softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);
        softwareTestRepository.save(softwareTest);
    }
}
