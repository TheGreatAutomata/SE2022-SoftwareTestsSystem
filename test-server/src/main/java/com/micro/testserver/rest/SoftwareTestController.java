package com.micro.testserver.rest;

import com.micro.api.DelegationsApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.dto.*;
import com.micro.testserver.mapper.TestSchemeMapper;
import com.micro.testserver.model.SoftwareTest;
import com.micro.testserver.model.SoftwareTestScheme;
import com.micro.testserver.model.SoftwareTestState;
import com.micro.testserver.repository.DelegationRepository;
import com.micro.testserver.repository.SoftwareTestRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SoftwareTestController implements DelegationsApi {
    @Autowired
    TestSchemeMapper testSchemeMapper;

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Override
    public ResponseEntity<Void> uploadTestScheme(String id, TestSchemeDto testSchemeDto) {
        //首先检查是否有这个委托，且委托是否已经进入可以编写测试方案的阶段
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Delegation delegation=delegation_op.get();
        DelegationState delegationState=delegation.getState();
        //目前暂定在等待市场部完成合同后就算完成
        if(!delegationState.equals(DelegationState.ACCEPTED)){
            return new ResponseEntity<>(HttpStatus.valueOf(400));
        }
        SoftwareTestScheme scheme=testSchemeMapper.toObj(testSchemeDto);
        //已经有流程了，可能是在重新修改
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",delegation.getDelegationId())!=null){
             Task task=taskService.createTaskQuery().taskName("UploadTestScheme").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",delegation.getDelegationId()).singleResult();
             if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
             }
             SoftwareTest test=runtimeService.getVariable(task.getExecutionId(),"softwareTest",SoftwareTest.class);
             test.setScheme(scheme);
             test.setState(SoftwareTestState.AUDIT_QUALITY);
             softwareTestRepository.save(test);
             runtimeService.setVariable(task.getExecutionId(),"softwareTest",test);
             return new ResponseEntity<>(HttpStatus.OK);
        }

        SoftwareTest softwareTest=new SoftwareTest();
        softwareTest.setDelegation_id(delegation.getDelegationId());
        softwareTest.setScheme(scheme);
        softwareTest.setState(SoftwareTestState.AUDIT_QUALITY);

        Map<String,Object> variables=new HashMap<>();

        variables.put("delegationId",delegation.getDelegationId());
        variables.put("softwareTest",softwareTest);


        softwareTestRepository.save(softwareTest);

        runtimeService.startProcessInstanceByKey("test_apply",variables);



        return DelegationsApi.super.uploadTestScheme(id, testSchemeDto);
    }

    @Override
    public ResponseEntity<TestSchemeDto> getTestScheme(String id) {

        return DelegationsApi.super.getTestScheme(id);
    }


    //前有屎山

    @Override
    public ResponseEntity<TestCaseDto> getDocTestCase(String id) {
        return DelegationsApi.super.getDocTestCase(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestcase(String id, TestCaseDto testCaseDto) {
        return DelegationsApi.super.uploadDocTestcase(id, testCaseDto);
    }

    @Override
    public ResponseEntity<TestRecordDto> getDocTestRecord(String id) {
        return DelegationsApi.super.getDocTestRecord(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestRecord(String id, TestRecordDto testRecordDto) {
        return DelegationsApi.super.uploadDocTestRecord(id, testRecordDto);
    }

    @Override
    public ResponseEntity<BugListDto> getDocBugList(String id) {
        return DelegationsApi.super.getDocBugList(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocBugList(String id, BugListDto bugListDto) {
        return DelegationsApi.super.uploadDocBugList(id, bugListDto);
    }

    @Override
    public ResponseEntity<DocEvaluationTableDto> getDocDocEvaluation(String id) {
        return DelegationsApi.super.getDocDocEvaluation(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocDocEvaluation(String id, DocEvaluationTableDto docEvaluationTableDto) {
        return DelegationsApi.super.uploadDocDocEvaluation(id, docEvaluationTableDto);
    }

    @Override
    public ResponseEntity<TestReportDto> getDocTestReport(String id) {
        return DelegationsApi.super.getDocTestReport(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestReport(String id, TestReportDto testReportDto) {
        return DelegationsApi.super.uploadDocTestReport(id, testReportDto);
    }

    @Override
    public ResponseEntity<TestReportEvaluationTableDto> getDocReportEvaluation(String id) {
        return DelegationsApi.super.getDocReportEvaluation(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocReportEvaluation(String id, TestReportEvaluationTableDto testReportEvaluationTableDto) {
        return DelegationsApi.super.uploadDocReportEvaluation(id, testReportEvaluationTableDto);
    }

    @Override
    public ResponseEntity<WorkEvaluationTableDto> getDocWorkEvaluation(String id) {
        return DelegationsApi.super.getDocWorkEvaluation(id);
    }

    @Override
    public ResponseEntity<Void> uploadDocWorkEvaluation(String id, WorkEvaluationTableDto workEvaluationTableDto) {
        return DelegationsApi.super.uploadDocWorkEvaluation(id, workEvaluationTableDto);
    }

    @Override
    public ResponseEntity<FormalTestReportDto> getDocFormalTestReport(String id) {
        return DelegationsApi.super.getDocFormalTestReport(id);
    }

}
