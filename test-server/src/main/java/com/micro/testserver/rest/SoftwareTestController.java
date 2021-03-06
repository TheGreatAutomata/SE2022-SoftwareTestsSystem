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
    @Autowired
    SoftwareReportMinioItemMapper softwareReportMinioItemMapper;

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testSchemeDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadTestScheme(String usrName, String usrId, String usrRole,String id, TestSchemeDto testSchemeDto) {
        //????????????????????????????????????????????????????????????????????????????????????????????????
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Delegation delegation=delegation_op.get();
        DelegationState delegationState=delegation.getState();
        //?????????????????????????????????????????????????????????
        //if(!delegationState.equals(DelegationState.ACCEPTED)){
        //    return new ResponseEntity<>(HttpStatus.valueOf(400));
        //}
        SoftwareTestScheme scheme=testSchemeMapper.toObj(testSchemeDto);
        //??????????????????
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

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<TestSchemeDto> getTestScheme(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(testSchemeMapper.toDto(softwareTest.getScheme()),HttpStatus.OK);
    }

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testSchemeDto (optional)
     * ??????????????????
     * @return
     */
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

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ??????????????????
     * @return
     */
    //????????????
    @Override
    public ResponseEntity<TestCaseDto> getDocTestCase(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareTestCaseMapper.toDto(softwareTest.getTestCase()),HttpStatus.OK);
    }

    /**
     * @param usrName     (required)
     * @param usrId       (required)
     * @param usrRole     (required)
     * @param id          The id of delegation (required)
     * @param testCaseDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocTestcase(String usrName, String usrId, String usrRole,String id, TestCaseDto testCaseDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestcase").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
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

    /**
     * @param usrName     (required)
     * @param usrId       (required)
     * @param usrRole     (required)
     * @param id          The id of delegation (required)
     * @param testCaseDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> putDocTestcase(String usrName, String usrId, String usrRole,String id, TestCaseDto testCaseDto) {
        //??????
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //??????????????????
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_TEST_CASE)){
            //????????????
            softwareTest.setTestCase(softwareTestCaseMapper.toObj(testCaseDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<TestRecordDto> getDocTestRecord(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareTestRecordMapper.toDto(softwareTest.getTestRecord()),HttpStatus.OK);
    }

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testRecordDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocTestRecord(String usrName, String usrId, String usrRole,String id, TestRecordDto testRecordDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestRecord").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
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

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testRecordDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> putDocTestRecord(String usrName, String usrId, String usrRole,String id, TestRecordDto testRecordDto) {
        //??????
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //??????????????????
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_TEST_RECORD)){
            //????????????
            softwareTest.setTestRecord(softwareTestRecordMapper.toObj(testRecordDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<BugListDto> getDocBugList(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareBugListMapper.toDto(softwareTest.getBugList()),HttpStatus.OK);
    }

    /**
     * @param usrName    (required)
     * @param usrId      (required)
     * @param usrRole    (required)
     * @param id         The id of delegation (required)
     * @param bugListDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocBugList(String usrName, String usrId, String usrRole,String id, BugListDto bugListDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadBugList").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
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

    /**
     * @param usrName    (required)
     * @param usrId      (required)
     * @param usrRole    (required)
     * @param id         The id of delegation (required)
     * @param bugListDto (optional)
     * ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> putDocBugList(String usrName, String usrId, String usrRole,String id, BugListDto bugListDto) {
        //??????
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //??????????????????
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_BUG_LIST)){
            //????????????
            softwareTest.setBugList(softwareBugListMapper.toObj(bugListDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ???????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<DocEvaluationTableDto> getDocDocEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareDocEvaluationTableMapper.toDto(softwareTest.getDocEvaluationTable()),HttpStatus.OK);
    }

    /**
     * @param usrName               (required)
     * @param usrId                 (required)
     * @param usrRole               (required)
     * @param id                    The id of delegation (required)
     * @param docEvaluationTableDto (optional)
     * ???????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocDocEvaluation(String usrName, String usrId, String usrRole,String id, DocEvaluationTableDto docEvaluationTableDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_apply").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadDocEvaluationTable").processDefinitionKey("test_apply").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setDocEvaluationTable(softwareDocEvaluationTableMapper.toObj(docEvaluationTableDto));
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT);
            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            //upload,????????????????????????
            Map<String,Object> variables=new HashMap<>();
            variables.put("softwareTest",softwareTest);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("test_audit",variables);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param usrName               (required)
     * @param usrId                 (required)
     * @param usrRole               (required)
     * @param id                    The id of delegation (required)
     * @param docEvaluationTableDto (optional)
     * ???????????????????????????
     * @return
     */
    /*@GetMapping("/test/test/test")
    public void dkd(){
        System.out.println("?????????");
        Map<String,Object> variables=new HashMap<>();
        SoftwareTest softwareTest=new SoftwareTest();
        softwareTest.setDelegation_id("629d9a7bb90a5669b8f5e2db");
        variables.put("softwareTest",softwareTest);
        variables.put("delegationId","629d9a7bb90a5669b8f5e2db");
        runtimeService.startProcessInstanceByKey("test_audit",variables);
    }*/
    @Override
    public ResponseEntity<Void> putDocDocEvaluation(String usrName, String usrId, String usrRole,String id, DocEvaluationTableDto docEvaluationTableDto) {
        //??????
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //??????????????????
        if(softwareTestService.checkModifiable(softwareTest.getState(),SoftwareTestState.TEST_DOC_DOC_EVALUATION_TABLE)){
            //????????????
            softwareTest.setDocEvaluationTable(softwareDocEvaluationTableMapper.toObj(docEvaluationTableDto));
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<TestReportDto> getDocTestReport(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareTestReportMapper.toDto(softwareTest.getTestReport()),HttpStatus.OK);
    }

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testReportDto (optional)
     * ????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocTestReport(String usrName, String usrId, String usrRole,String id, TestReportDto testReportDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id)!=null){
            Task task=taskService.createTaskQuery().taskName("UploadTestReport").processDefinitionKey("test_audit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            System.out.println("uploadTestReport");
            System.out.println(testReportDto);
            SoftwareTestReport report=softwareTestReportMapper.toObj(testReportDto);
            SoftwareTestReport reportToImp=softwareTest.getTestReport();
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????Email(report.get????????????Email());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            softwareTest.setTestReport(reportToImp);
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);
            System.out.println(softwareTest);
            softwareTestRepository.save(softwareTest);
            runtimeService.setVariable(task.getExecutionId(),"softwareTest",softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testReportDto (optional)
     * ????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> putDocTestReport(String usrName, String usrId, String usrRole, String id, TestReportDto testReportDto) {
        //????????????????????????
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id)!=null){
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SoftwareTestState softwareTestState=softwareTest.getState();
            if(!softwareTestState.equals(SoftwareTestState.TEST_DOC_TEST_REPORT)){//&&!softwareTestState.equals(SoftwareTestState.TEST_REPORT_DENIED)&&!softwareTestState.equals(SoftwareTestState.TEST_DOC_WORK_DENIED)){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTestReport report=softwareTestReportMapper.toObj(testReportDto);
            SoftwareTestReport reportToImp=softwareTest.getTestReport();
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set?????????(report.get?????????());
            reportToImp.set???????????????(report.get???????????????());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????Email(report.get????????????Email());
            reportToImp.set??????????????????(report.get??????????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            reportToImp.set????????????(report.get????????????());
            softwareTest.setTestReport(reportToImp);
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);
            softwareTestRepository.save(softwareTest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ???????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<TestReportEvaluationTableDto> getDocReportEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareReportEvaluationMapper.toDto(softwareTest.getReportEvaluationTable()),HttpStatus.OK);
    }

    /**
     * @param usrName                      (required)
     * @param usrId                        (required)
     * @param usrRole                      (required)
     * @param id                           The id of delegation (required)
     * @param testReportEvaluationTableDto (optional)
     * ???????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocReportEvaluation(String usrName, String usrId, String usrRole,String id, TestReportEvaluationTableDto testReportEvaluationTableDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id).singleResult()!=null){
            Task task=taskService.createTaskQuery().taskName("UploadReportEvaluationTable").processDefinitionKey("test_audit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setReportEvaluationTable(softwareReportEvaluationMapper.toObj(testReportEvaluationTableDto));
            //????????????
            boolean accepted=testReportEvaluationTableDto.get????????????().equals("??????");
            if(accepted){
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_EVALUATION_TABLE);
            }else{
                softwareTest.setState(SoftwareTestState.TEST_REPORT_DENIED);
            }
            runtimeService.setVariable(task.getExecutionId(),"accepted",accepted);
            softwareTestRepository.save(softwareTest);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (runtimeService.createProcessInstanceQuery().processDefinitionKey("test_reaudit").variableValueEquals("delegationId",id).singleResult()!=null) {
            Task task=taskService.createTaskQuery().taskName("UploadReportEvaluationTable").processDefinitionKey("test_reaudit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setReportEvaluationTable(softwareReportEvaluationMapper.toObj(testReportEvaluationTableDto));
            //????????????
            boolean accepted=testReportEvaluationTableDto.get????????????().equals("??????");
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

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ?????????????????????
     * @return
     */
    @Override
    public ResponseEntity<WorkEvaluationTableDto> getDocWorkEvaluation(String usrName, String usrId, String usrRole,String id) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
        //???????????????????????????
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareWorkEvaluationTableMapper.toDto(softwareTest.getWorkEvaluationTable()),HttpStatus.OK);
    }

    /**
     * @param usrName                (required)
     * @param usrId                  (required)
     * @param usrRole                (required)
     * @param id                     The id of delegation (required)
     * @param workEvaluationTableDto (optional)
     * ?????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadDocWorkEvaluation(String usrName, String usrId, String usrRole,String id, WorkEvaluationTableDto workEvaluationTableDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id).singleResult()!=null){
            System.out.println(0.5);
            Task task=taskService.createTaskQuery().taskName("UploadWorkEvaluationTable").processDefinitionKey("test_audit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                System.out.println(1);
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            softwareTest.setWorkEvaluationTable(softwareWorkEvaluationTableMapper.toObj(workEvaluationTableDto));
            //????????????
            boolean accepted=workEvaluationTableDto.get?????????????????????().equals("????????????");
            if(accepted){
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);
            }else{
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_DENIED);
            }
            System.out.println(2);
            softwareTestRepository.save(softwareTest);
            runtimeService.setVariable(task.getExecutionId(),"softwareTest",softwareTest);

            int acceptSymbol=0;
            if(accepted){
                acceptSymbol=1;
            }

            System.out.println(softwareTest);
            runtimeService.setVariable(task.getExecutionId(),"workAccepted",acceptSymbol);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (runtimeService.createProcessInstanceQuery().processDefinitionKey("test_reaudit").variableValueEquals("delegationId",id).singleResult()!=null) {
            Task task=taskService.createTaskQuery().taskName("UploadWorkEvaluationTable").processDefinitionKey("test_reaudit").processVariableValueEquals("delegationId",id).singleResult();
            if(task==null){
                System.out.println(3);
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            //???????????????????????????
            if(softwareTest==null){
                System.out.println(4);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            System.out.println(5);
            softwareTest.setWorkEvaluationTable(softwareWorkEvaluationTableMapper.toObj(workEvaluationTableDto));
            //????????????
            boolean accepted=workEvaluationTableDto.get?????????????????????().equals("????????????");
            if(accepted){
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);
            }else{
                softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_DENIED);
            }

            int acceptSymbol=0;
            if(accepted){
                acceptSymbol=1;
            }

            System.out.println("?????????????????????");
            System.out.println(softwareTest);
            softwareTestRepository.save(softwareTest);
            runtimeService.setVariable(task.getExecutionId(),"softwareTest",softwareTest);
            runtimeService.setVariable(task.getExecutionId(),"workAccepted",acceptSymbol);
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param usrName       (required)
     * @param usrId         (required)
     * @param usrRole       (required)
     * @param id            The id of delegation (required)
     * @param testReportDto (optional)
     * ?????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> putDocApplyReportEvaluation(String usrName, String usrId, String usrRole,String id,TestReportDto testReportDto) {
        if(runtimeService.createProcessInstanceQuery().processDefinitionKey("test_audit").variableValueEquals("delegationId",id).singleResult()==null){
            //??????????????????????????????????????????
            SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(id);
            if(softwareTest==null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //???????????????????????????????????????????????????
            if(!softwareTest.getState().equals(SoftwareTestState.TEST_REPORT_DENIED) && !softwareTest.getState().equals(SoftwareTestState.TEST_DOC_WORK_DENIED) && !softwareTest.getState().equals(SoftwareTestState.TEST_DOC_WORK_DENIED)){
                return new ResponseEntity<>(HttpStatus.valueOf(400));
            }
            System.out.println("REAUDIT");
            System.out.println(testReportDto);
            SoftwareTestReport report=softwareTestReportMapper.toObj(testReportDto);
            SoftwareTestReport reportToImp=softwareTest.getTestReport();
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get??????????????????()!=null)
                reportToImp.set??????????????????(report.get??????????????????());
            if(report.get??????????????????()!=null)
                reportToImp.set??????????????????(report.get??????????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get?????????()!=null)
                reportToImp.set?????????(report.get?????????());
            if(report.get???????????????()!=null)
                reportToImp.set???????????????(report.get???????????????());
            if(report.get?????????()!=null)
                reportToImp.set?????????(report.get?????????());
            if(report.get???????????????()!=null)
                reportToImp.set???????????????(report.get???????????????());
            if(report.get?????????()!=null)
                reportToImp.set?????????(report.get?????????());
            if(report.get???????????????()!=null)
                reportToImp.set???????????????(report.get???????????????());
            if(report.get??????????????????()!=null)
                reportToImp.set??????????????????(report.get??????????????????());
            if(report.get????????????Email()!=null)
                reportToImp.set????????????Email(report.get????????????Email());
            if(report.get??????????????????()!=null)
                reportToImp.set??????????????????(report.get??????????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            if(report.get????????????()!=null)
                reportToImp.set????????????(report.get????????????());
            softwareTest.setTestReport(reportToImp);
            softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);
            System.out.println(softwareTest);
            softwareTestRepository.save(softwareTest);
            Map<String,Object> variables=new HashMap<>();
            variables.put("softwareTest",softwareTest);
            variables.put("delegationId",id);
            runtimeService.startProcessInstanceByKey("test_reaudit",variables);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * @param usrName                 (required)
     * @param usrId                   (required)
     * @param usrRole                 (required)
     * @param id                      The id of delegation (required)
     * @param testSchemeAuditTableDto (optional)
     * ???????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<Void> uploadTestSchemeAuditTable(String usrName, String usrId, String usrRole,String id, TestSchemeAuditTableDto testSchemeAuditTableDto) {
        //???????????????????????????????????????????????????
        //????????????????????????????????????????????????
        Optional<Delegation> delegationOptional=delegationRepository.findById(id);
        if(delegationOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task=taskService.createTaskQuery().processDefinitionKey("test_apply").taskName("AuditTestScheme").processVariableValueEquals("delegationId",id).singleResult();
        if(task==null){
            //???????????????
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        SchemeEvaluationTable evaluationTable=testSchemeAuditTableMapper.toObj(testSchemeAuditTableDto);
        SoftwareTest test=softwareTestRepository.findByDelegationId(id);
        if(test==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        test.setSchemeEvaluationTable(evaluationTable);
        boolean result=testSchemeAuditTableDto.get????????????().equals("??????");
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

    /**
     * @param usrName (required)
     * @param usrId   (required)
     * @param usrRole (required)
     * @param id      The id of delegation (required)
     * ???????????????????????????
     * @return
     */
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

    /**
     * @param usrName      (required)
     * @param usrId        (required)
     * @param usrRole      (required)
     * @param delegationId The id of delegation (required)
     * @param projectId    The id of project (required)
     * ????????????
     * @return
     */
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
        softwareTest.set????????????(delegation_op.get().applicationTable.get????????????());
        softwareTestRepository.save(softwareTest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param usrId   (required)
     * @param usrName (required)
     * @param usrRole (required)
     * ?????????????????????????????????
     * @return
     */
    @Override
    public ResponseEntity<List<TestProjectDto>> listProjects(String usrId, String usrName, String usrRole) {
        List<SoftwareTest> softwareTests=softwareTestRepository.findByUsrId(usrId);
        if(softwareTests.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<TestProjectDto> projectDtos=new ArrayList<>();
        for (SoftwareTest softwareTest:softwareTests){
            Optional<Delegation> delegation_op=delegationRepository.findById(softwareTest.getDelegation_id());
            TestProject project=new TestProject(softwareTest.getUsrId(),softwareTest.getUsrName(),softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),delegation_op.get().getApplicationTable().get????????????());
            projectDtos.add(testProjectMapper.toDto(project));
        }
        return new ResponseEntity<>(projectDtos,HttpStatus.OK);
    }

    /**
     * @param usrId   (required)
     * @param usrName (required)
     * @param usrRole (required)
     *  ??????????????????
     * @return
     */
    @Override
    public ResponseEntity<List<TestProjectDto>> listAllProjects(String usrId, String usrName, String usrRole) {
        List<SoftwareTest> softwareTests=softwareTestRepository.findAll();
        if(softwareTests.size()==0){
            return new ResponseEntity<>(HttpStatus.valueOf(400));
        }
        List<TestProjectDto> projectDtos=new ArrayList<>();
        for(SoftwareTest softwareTest:softwareTests){
            Optional<Delegation> delegation_op=delegationRepository.findById(softwareTest.getDelegation_id());
            TestProject project=new TestProject(softwareTest.getUsrId(),softwareTest.getUsrName(),softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),delegation_op.get().getApplicationTable().get????????????());
            projectDtos.add(testProjectMapper.toDto(project));
        }
        return new ResponseEntity<>(projectDtos,HttpStatus.OK);
    }

    /**
     * @param usrName      (required)
     * @param usrId        (required)
     * @param usrRole      (required)
     * @param delegationId (required)
     * ????????????id????????????
     * @return
     */
    @Override
    public ResponseEntity<TestProjectDto> findProjectByDelegationId(String usrName, String usrId, String usrRole,String delegationId) {
        SoftwareTest softwareTest=softwareTestRepository.findByDelegationId(delegationId);
        Optional<Delegation> delegation_op=delegationRepository.findById(softwareTest.getDelegation_id());
        if(softwareTest==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TestProject project=new TestProject(softwareTest.getUsrId(),softwareTest.getUsrName(),softwareTest.getDelegation_id(),softwareTest.getContract().getContractId(),softwareTest.getProjectId(),softwareTest.getState(),delegation_op.get().getApplicationTable().get????????????());
        TestProjectDto dto=testProjectMapper.toDto(project);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    /**
     * @param usrId        (required)
     * @param usrName      (required)
     * @param usrRole      (required)
     * @param delegationId (required)
     * ??????PDF??????
     * @return
     */
    @Override
    public ResponseEntity<SoftwareSingleFileDto> findLatexReportByDelegationId(String usrId, String usrName, String usrRole, String delegationId) {
        Optional<Delegation> delegationOptional=delegationRepository.findById(delegationId);
        System.out.println("????????????");
        if(delegationOptional.isEmpty()){
            System.out.println("???????????????");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Delegation delegation=delegationOptional.get();
        MinioFileItem item=softwareTestService.getReportFile(delegation.getProjectId());
        SoftwareSingleFileDto dto= softwareReportMinioItemMapper.toDto(item);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    /*@GetMapping("/new/test")
    public void getContractTest(){
        Contract c = restTemplate.getForObject("http://contract-server/contract/", Contract.class);
        System.out.println(c);
    }*/
}
