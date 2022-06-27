package com.micro.testserver.rest;

import com.micro.api.TestApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.dto.*;
import com.micro.testserver.mapper.*;
import com.micro.testserver.model.SchemeEvaluationTable;
import com.micro.testserver.model.SoftwareTest;
import com.micro.testserver.model.SoftwareTestScheme;
import com.micro.testserver.model.SoftwareTestState;
import com.micro.testserver.repository.DelegationRepository;
import com.micro.testserver.repository.SoftwareTestRepository;
import com.micro.testserver.service.SoftwareTestService;
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
public class SoftwareTestController implements TestApi {
    @Autowired
    TestSchemeMapper testSchemeMapper;

    @Autowired
    SoftwareBugListMapper softwareBugListMapper;

    @Autowired
    SoftwareDocEvaluationTableMapper softwareDocEvaluationTableMapper;

    @Autowired
    SoftwareFormalTestReportMapper softwareFormalTestReportMapper;

    @Autowired
    SoftwareReportEvaluationMapper softwareReportEvaluationMapper;

    @Autowired
    SoftwareTestCaseMapper softwareTestCaseMapper;

    @Autowired
    SoftwareTestRecordMapper softwareTestRecordMapper;

    @Autowired
    SoftwareTestReportMapper softwareTestReportMapper;

    @Autowired
    SoftwareWorkEvaluationTableMapper softwareWorkEvaluationTableMapper;

    @Autowired
    TestSchemeAuditTableMapper testSchemeAuditTableMapper;

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    SoftwareTestService softwareTestService;


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
        //if(!delegationState.equals(DelegationState.ACCEPTED)){
        //    return new ResponseEntity<>(HttpStatus.valueOf(400));
        //}
        SoftwareTestScheme scheme=testSchemeMapper.toObj(testSchemeDto);
        //已经有流程了
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",delegation.getDelegationId()).singleResult()!=null){
            return new ResponseEntity<>(HttpStatus.valueOf(400));
        }

        SoftwareTest softwareTest=new SoftwareTest();
        softwareTest.setDelegation_id(delegation.getDelegationId());
        softwareTest.setScheme(scheme);
        softwareTest.setState(SoftwareTestState.AUDIT_QUALITY);

        Map<String,Object> variables=new HashMap<>();

        variables.put("delegationId",delegation.getDelegationId());


        softwareTestRepository.save(softwareTest);

        runtimeService.startProcessInstanceByKey("test_apply",variables);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestSchemeDto> getTestScheme(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(testSchemeMapper.toDto(softwareTest.getScheme()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putTestScheme(String id, TestSchemeDto testSchemeDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestScheme").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }

            SoftwareTest test=softwareTestRepository.findByDelegationId(id);
            if(test==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SoftwareTestState softwareTestState=test.getState();
            if(!(softwareTestState.equals(SoftwareTestState.AUDIT_QUALITY) || softwareTestState.equals(SoftwareTestState.AUDIT_QUALITY_DENIED))){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }

            SoftwareTestScheme scheme=testSchemeMapper.toObj(testSchemeDto);
            test.setScheme(scheme);
            test.setState(SoftwareTestState.AUDIT_QUALITY);

            taskService.complete(task.getId());

            softwareTestRepository.save(test);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    //前有屎山

    @Override
    public ResponseEntity<TestCaseDto> getDocTestCase(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestCaseMapper.toDto(softwareTest.getTestCase()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestcase(String id, TestCaseDto testCaseDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestcase").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();

            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setTestCase(softwareTestCaseMapper.toObj(testCaseDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_RECORD);

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> putDocTestcase(String id, TestCaseDto testCaseDto) {
        //检查
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //检查测试状态
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_TEST_CASE)){
            //可以修改
            softwareTest.setTestCase(softwareTestCaseMapper.toObj(testCaseDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<TestRecordDto> getDocTestRecord(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestRecordMapper.toDto(softwareTest.getTestRecord()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestRecord(String id, TestRecordDto testRecordDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestRecord").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setTestRecord(softwareTestRecordMapper.toObj(testRecordDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_BUG_LIST);

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> putDocTestRecord(String id, TestRecordDto testRecordDto) {
        //检查
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //检查测试状态
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_TEST_RECORD)){
            //可以修改
            softwareTest.setTestRecord(softwareTestRecordMapper.toObj(testRecordDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }


    @Override
    public ResponseEntity<BugListDto> getDocBugList(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareBugListMapper.toDto(softwareTest.getBugList()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocBugList(String id, BugListDto bugListDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadBugList").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setBugList(softwareBugListMapper.toObj(bugListDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_DOC_EVALUATION_TABLE);

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> putDocBugList(String id, BugListDto bugListDto) {
        //检查
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //检查测试状态
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_BUG_LIST)){
            //可以修改
            softwareTest.setBugList(softwareBugListMapper.toObj(bugListDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<DocEvaluationTableDto> getDocDocEvaluation(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareDocEvaluationTableMapper.toDto(softwareTest.getDocEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocDocEvaluation(String id, DocEvaluationTableDto docEvaluationTableDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadDocEvaluationTable").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setDocEvaluationTable(softwareDocEvaluationTableMapper.toObj(docEvaluationTableDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT);
            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            //upload,自动开启审核流程

            Map<String,Object> variables=new HashMap<>();
            variables.put("softwareTest",softwareTest);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("test_audit",variables);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> putDocDocEvaluation(String id, DocEvaluationTableDto docEvaluationTableDto) {
        //检查
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //检查测试状态
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_DOC_EVALUATION_TABLE)){
            //可以修改
            softwareTest.setDocEvaluationTable(softwareDocEvaluationTableMapper.toObj(docEvaluationTableDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<TestReportDto> getDocTestReport(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestReportMapper.toDto(softwareTest.getTestReport()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestReport(String id, TestReportDto testReportDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestReport").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setTestReport(softwareTestReportMapper.toObj(testReportDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TestReportEvaluationTableDto> getDocReportEvaluation(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareReportEvaluationMapper.toDto(softwareTest.getReportEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocReportEvaluation(String id, TestReportEvaluationTableDto testReportEvaluationTableDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id).singleResult()!=null){
            Task task=taskService.createTaskQuery().taskName("UploadReportEvaluationTable").processDefinitionKey("test_audit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setReportEvaluationTable(softwareReportEvaluationMapper.toObj(testReportEvaluationTableDto));

            //审核结果
            boolean accepted=true;
            if(accepted){
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_EVALUATION_TABLE);
            }else{
                softwareTest.setState(SoftwareTestState.TEST_REPORT_DENIED);
            }
            runtimeService.setVariable(task.getExecutionId(),"accepted",accepted);

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<WorkEvaluationTableDto> getDocWorkEvaluation(String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareWorkEvaluationTableMapper.toDto(softwareTest.getWorkEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocWorkEvaluation(String id, WorkEvaluationTableDto workEvaluationTableDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadWorkEvaluationTable").processDefinitionKey("test_audit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

            //压根没有这么个测试
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setWorkEvaluationTable(softwareWorkEvaluationTableMapper.toObj(workEvaluationTableDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);

            //审核结果
            boolean accepted=true;
            if(accepted){
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);
            }else{
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_DENIED);
            }

            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> putDocApplyReportEvaluation(String id) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id).singleResult()==null){
            //同一时间最多只有一个审核流程
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            if(softwareTest==null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //必须在被拒绝后才能主动开启审核流程
            if(!softwareTest.getState().equals(SoftwareTestState.TEST_REPORT_DENIED) && !softwareTest.getState().equals(SoftwareTestState.TEST_DOC_WORK_DENIED)){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            Map<String,Object> variables=new HashMap<>();
            variables.put("softwareTest",softwareTest);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("test_audit",variables);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> uploadTestSchemeAuditTable(String id, TestSchemeAuditTableDto testSchemeAuditTableDto) {
        //该接口仅仅用于质量部人员上传评审表
        //当然，也需要判断一下委托存不存在
        Optional<Delegation> delegationOptional=delegationRepository.findById(id);
        if(delegationOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task=taskService.createTaskQuery().processDefinitionKey("test_apply").taskName("AuditTestScheme").processVariableValueEquals("delegationId",id).singleResult();
        if(task==null){
            //压根没开始
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SchemeEvaluationTable evaluationTable=testSchemeAuditTableMapper.toObj(testSchemeAuditTableDto);
        SoftwareTest test=softwareTestRepository.findByDelegationId(id);
        if(test==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        test.setSchemeEvaluationTable(evaluationTable);

        boolean result=true;
        if(result){
            test.setState(SoftwareTestState.TEST_DOC_TEST_CASE);
        }else{
            test.setState(SoftwareTestState.AUDIT_QUALITY_DENIED);
        }



        softwareTestRepository.save(test);
        runtimeService.setVariable(task.getExecutionId(),"auditResult",result);

        taskService.complete(task.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestSchemeAuditTableDto> getTestSchemeAuditTable(String id) {
        return TestApi.super.getTestSchemeAuditTable(id);
    }
}
