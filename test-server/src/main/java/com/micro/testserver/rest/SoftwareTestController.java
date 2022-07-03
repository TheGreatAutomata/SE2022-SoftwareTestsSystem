package com.micro.testserver.rest;

import com.micro.api.TestApi;
import com.micro.contractserver.mapper.ContractMapper;
import com.micro.contractserver.model.Contract;
import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.model.User;
import com.micro.dto.*;
import com.micro.testserver.mapper.*;
import com.micro.testserver.model.*;
import com.micro.testserver.repository.DelegationRepository;
import com.micro.testserver.repository.SoftwareTestRepository;
import com.micro.testserver.repository.TestContractRepo;
import com.micro.testserver.service.SoftwareTestService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.*;

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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TestProjectMapper testProjectMapper;

    @Autowired
    ContractMapper contractMapper;

    @Override
    public ResponseEntity<Void> uploadTestScheme(String usrName, String usrId, String usrRole,String id, TestSchemeDto testSchemeDto) {
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

        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        if (softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity<TestSchemeDto> getTestScheme(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(testSchemeMapper.toDto(softwareTest.getScheme()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> putTestScheme(String usrName, String usrId, String usrRole,String id, TestSchemeDto testSchemeDto) {
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
    public ResponseEntity<TestCaseDto> getDocTestCase(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestCaseMapper.toDto(softwareTest.getTestCase()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestcase(String usrName, String usrId, String usrRole,String id, TestCaseDto testCaseDto) {
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
    public ResponseEntity<Void> putDocTestcase(String usrName, String usrId, String usrRole,String id, TestCaseDto testCaseDto) {
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
    public ResponseEntity<TestRecordDto> getDocTestRecord(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestRecordMapper.toDto(softwareTest.getTestRecord()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestRecord(String usrName, String usrId, String usrRole,String id, TestRecordDto testRecordDto) {
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
    public ResponseEntity<Void> putDocTestRecord(String usrName, String usrId, String usrRole,String id, TestRecordDto testRecordDto) {
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
    public ResponseEntity<BugListDto> getDocBugList(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareBugListMapper.toDto(softwareTest.getBugList()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocBugList(String usrName, String usrId, String usrRole,String id, BugListDto bugListDto) {
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
    public ResponseEntity<Void> putDocBugList(String usrName, String usrId, String usrRole,String id, BugListDto bugListDto) {
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
    public ResponseEntity<DocEvaluationTableDto> getDocDocEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareDocEvaluationTableMapper.toDto(softwareTest.getDocEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocDocEvaluation(String usrName, String usrId, String usrRole,String id, DocEvaluationTableDto docEvaluationTableDto) {
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
    public ResponseEntity<Void> putDocDocEvaluation(String usrName, String usrId, String usrRole,String id, DocEvaluationTableDto docEvaluationTableDto) {
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
    public ResponseEntity<TestReportDto> getDocTestReport(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareTestReportMapper.toDto(softwareTest.getTestReport()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocTestReport(String usrName, String usrId, String usrRole,String id, TestReportDto testReportDto) {
        //检测当前有无流程
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id)!=null){
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
    public ResponseEntity<TestReportEvaluationTableDto> getDocReportEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareReportEvaluationMapper.toDto(softwareTest.getReportEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocReportEvaluation(String usrName, String usrId, String usrRole,String id, TestReportEvaluationTableDto testReportEvaluationTableDto) {
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
            boolean accepted=testReportEvaluationTableDto.get确认意见().equals("通过");
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
    public ResponseEntity<WorkEvaluationTableDto> getDocWorkEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);

        //压根没有这么个测试
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(softwareWorkEvaluationTableMapper.toDto(softwareTest.getWorkEvaluationTable()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadDocWorkEvaluation(String usrName, String usrId, String usrRole,String id, WorkEvaluationTableDto workEvaluationTableDto) {
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
            boolean accepted=workEvaluationTableDto.get市场部审核意见().equals("批准签发");
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
    public ResponseEntity<Void> putDocApplyReportEvaluation(String usrName, String usrId, String usrRole,String id) {
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
    public ResponseEntity<Void> uploadTestSchemeAuditTable(String usrName, String usrId, String usrRole,String id, TestSchemeAuditTableDto testSchemeAuditTableDto) {
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

        boolean result=testSchemeAuditTableDto.get确认意见().equals("通过");

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
    public ResponseEntity<TestSchemeAuditTableDto> getTestSchemeAuditTable(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TestSchemeAuditTableDto schemeAuditTableDto=testSchemeAuditTableMapper.toDto(softwareTest.getSchemeEvaluationTable());
        return new ResponseEntity<>(schemeAuditTableDto,HttpStatus.OK);
    }

    @Autowired
    TestContractRepo testContractRepo;

    @Override
    public ResponseEntity<Void> prepareProject(String usrName, String usrId, String usrRole,String delegationId, String projectId) {
        Optional<Contract> c_op = testContractRepo.findByDelegationId(delegationId);
        Optional<Delegation> delegation_op=delegationRepository.findById(delegationId);
        if(c_op.isEmpty()||delegation_op.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Contract c=c_op.get();
        SoftwareTest softwareTest=new SoftwareTest();
        softwareTest.setDelegation_id(delegationId);
        softwareTest.setState(SoftwareTestState.TEST_SCHEME);
        softwareTest.setContract(c);
        softwareTest.setProjectId(projectId);
        softwareTest.setUsrId(c.getUsrId());
        softwareTest.setUsrName(c.getUsrName());
        softwareTest.set软件名称(delegation_op.get().applicationTable.get软件名称());

        softwareTestRepository.save(softwareTest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TestProjectDto>> listProjects(String usrId, String usrName, String usrRole) {
        List<SoftwareTest> softwareTests=softwareTestRepository.findByUsrId(usrId);
        if(softwareTests.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<TestProjectDto> projectDtos=new ArrayList<>();
        for (SoftwareTest softwareTest:softwareTests){
            TestProject project=new TestProject(usrId,usrName,softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),softwareTest.get软件名称());
            projectDtos.add(testProjectMapper.toDto(project));
        }
        return new ResponseEntity<>(projectDtos,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TestProjectDto>> listAllProjects(String usrId, String usrName, String usrRole) {
        System.out.println("getAll");
        List<SoftwareTest> softwareTests=softwareTestRepository.findAll();
        if(softwareTests.size()==0){
            return new ResponseEntity<>(HttpStatus.valueOf(400));
        }
        List<TestProjectDto> projectDtos=new ArrayList<>();
        for(SoftwareTest softwareTest:softwareTests){
            TestProject project=new TestProject(softwareTest.getUsrId(),softwareTest.getUsrName(),softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),softwareTest.get软件名称());
            projectDtos.add(testProjectMapper.toDto(project));
        }
        return new ResponseEntity<>(projectDtos,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestProjectDto> findProjectByDelegationId(String usrName, String usrId, String usrRole,String delegationId) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(delegationId);
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TestProject project=new TestProject(softwareTest.getUsrId(),softwareTest.getUsrName(),softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),softwareTest.get软件名称());
        TestProjectDto dto=testProjectMapper.toDto(project);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    /*@GetMapping("/new/test")
    public void getContractTest(){
        Contract c = restTemplate.getForObject("http://contract-server/contract/", Contract.class);
        System.out.println(c);
    }*/
}
