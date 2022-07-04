package com.micro.testserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.dto.*;
import com.micro.testserver.TestServerApplication;
import com.micro.testserver.mapper.*;
import com.micro.testserver.model.*;
import com.micro.testserver.repository.DelegationRepository;
import com.micro.testserver.repository.SoftwareTestRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestServerApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class SoftwareTestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SoftwareTestRepository softwareTestRepository;
    @MockBean
    private DelegationRepository delegationRepository;
    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskQuery taskQuery;
    @MockBean
    private TaskEntity taskEntity;
    @MockBean
    private RuntimeService runtimeService;
    @MockBean
    private ProcessInstanceQuery processInstanceQuery;
    @MockBean
    private ProcessInstance processInstance;

    @BeforeEach
    void init(){
        when(taskService.createTaskQuery())
                .thenReturn(taskQuery);
        when(runtimeService.createProcessInstanceQuery())
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.processDefinitionKey(Mockito.anyString()))
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.variableValueEquals(Mockito.anyString(),Mockito.any(Object.class)))
                .thenReturn(processInstanceQuery);
        when(processInstanceQuery.singleResult())
                .thenReturn(processInstance);
        ArrayList<ProcessInstance> instanceList=new ArrayList<>();
        instanceList.add(processInstance);
        when(processInstanceQuery.list())
                .thenReturn(instanceList);

        when(taskQuery.active())
                .thenReturn(taskQuery);
        when(taskQuery.processDefinitionKey(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.taskName(Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.processVariableValueEquals(Mockito.anyString(),Mockito.anyString()))
                .thenReturn(taskQuery);
        when(taskQuery.singleResult())
                .thenReturn(taskEntity);
        ArrayList<Task> list=new ArrayList<>();
        list.add(taskEntity);
        when(taskQuery.list())
                .thenReturn(list);
        when(taskEntity.getExecutionId())
                .thenReturn("123");

    }

    @Test
    public void setUp() throws Exception{
        when(softwareTestRepository.findByDelegationId("123"))
                .thenReturn(new SoftwareTest());

        mockMvc.perform(get("/test/{id}/test-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    @Test
    void uploadTestScheme() throws Exception {
        Delegation delegation=new Delegation();
        delegation.setDelegationId("123");
        delegation.setState(DelegationState.ACCEPTED);
        delegation.setUsrName("great");

        SoftwareTest softwareTest=new SoftwareTest();
        softwareTest.setDelegation_id(delegation.getDelegationId());

        TestSchemeDto testSchemeDto=new TestSchemeDto();
        testSchemeDto.set标识("新方案");

        String body=toJson(testSchemeDto);

        when(delegationRepository.findById("123"))
                .thenReturn(Optional.of(delegation));
        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);
        when(processInstanceQuery.singleResult())
                .thenReturn(null);

        mockMvc.perform(post("/test/{id}/test-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

    }
    @Test
    void getTestScheme() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestScheme testScheme=new SoftwareTestScheme();
        testScheme.setName("测试方案");

        softwareTest.setScheme(testScheme);

        TestSchemeMapper mapper=new TestSchemeMapperImpl();

        TestSchemeDto schemeDto=mapper.toDto(testScheme);

        String content=toJson(schemeDto);

        when(softwareTestRepository.findByDelegationId("123"))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }



    @Test
    void putTestScheme() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        softwareTest.setState(SoftwareTestState.AUDIT_QUALITY_DENIED);

        TestSchemeDto testSchemeDto=new TestSchemeDto();
        testSchemeDto.set标识("新方案");

        String body=toJson(testSchemeDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(put("/test/{id}/test-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);

        mockMvc.perform(put("/test/{id}/test-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    void getDocTestCase() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestCase testcase=new SoftwareTestCase();
        testcase.setName("测试用例");

        softwareTest.setTestCase(testcase);

        SoftwareTestCaseMapper mapper=new SoftwareTestCaseMapperImpl();

        TestCaseDto testCaseDto=mapper.toDto(testcase);

        String content=toJson(testCaseDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/test-case","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocTestcase() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestCase testcase=new SoftwareTestCase();
        testcase.setName("测试用例");

        softwareTest.setTestCase(testcase);

        SoftwareTestCaseMapper mapper=new SoftwareTestCaseMapperImpl();

        TestCaseDto testCaseDto=mapper.toDto(testcase);

        String body=toJson(testCaseDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/test-case","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void putDocTestcase() throws Exception{
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestCase testcase=new SoftwareTestCase();
        testcase.setName("测试用例");

        softwareTest.setTestCase(testcase);

        SoftwareTestCaseMapper mapper=new SoftwareTestCaseMapperImpl();

        TestCaseDto testCaseDto=mapper.toDto(testcase);

        String body=toJson(testCaseDto);

        softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_RECORD);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(put("/test/{id}/test-doc/test-case","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);

        mockMvc.perform(put("/test/{id}/test-doc/test-case","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    void getDocTestRecord() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestRecord testRecord=new SoftwareTestRecord();
        testRecord.setName("测试记录");

        softwareTest.setTestRecord(testRecord);

        SoftwareTestRecordMapper mapper=new SoftwareTestRecordMapperImpl();

        TestRecordDto testRecordDto=mapper.toDto(testRecord);

        String content=toJson(testRecordDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/test-record","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocTestRecord() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestRecord testRecord=new SoftwareTestRecord();
        testRecord.setName("测试记录");

        softwareTest.setTestRecord(testRecord);

        SoftwareTestRecordMapperImpl mapper=new SoftwareTestRecordMapperImpl();

        TestRecordDto testRecordDto=mapper.toDto(testRecord);

        String body=toJson(testRecordDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/test-record","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void putDocTestRecord() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestRecord testRecord=new SoftwareTestRecord();
        testRecord.setName("测试记录");

        softwareTest.setTestRecord(testRecord);

        SoftwareTestRecordMapperImpl mapper=new SoftwareTestRecordMapperImpl();

        TestRecordDto testRecordDto=mapper.toDto(testRecord);

        String body=toJson(testRecordDto);

        softwareTest.setState(SoftwareTestState.TEST_DOC_BUG_LIST);
        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(put("/test/{id}/test-doc/test-record","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);

        mockMvc.perform(put("/test/{id}/test-doc/test-record","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    void getDocBugList() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareBugList bugList=new SoftwareBugList();
        bugList.setName("错误列表");

        softwareTest.setBugList(bugList);

        SoftwareBugListMapper mapper=new SoftwareBugListMapperImpl();

        BugListDto bugListDto=mapper.toDto(bugList);

        String content=toJson(bugListDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/buglist","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocBugList() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareBugList bugList=new SoftwareBugList();
        bugList.setName("错误列表");

        softwareTest.setBugList(bugList);

        SoftwareBugListMapper mapper=new SoftwareBugListMapperImpl();

        BugListDto bugListDto=mapper.toDto(bugList);

        String body=toJson(bugListDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/buglist","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void putDocBugList() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareBugList bugList=new SoftwareBugList();
        bugList.setName("错误列表");

        softwareTest.setBugList(bugList);

        SoftwareBugListMapper mapper=new SoftwareBugListMapperImpl();

        BugListDto bugListDto=mapper.toDto(bugList);

        String body=toJson(bugListDto);

        softwareTest.setState(SoftwareTestState.TEST_DOC_DOC_EVALUATION_TABLE);
        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(put("/test/{id}/test-doc/buglist","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);

        mockMvc.perform(put("/test/{id}/test-doc/buglist","123")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    void getDocDocEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareDocEvaluationTable docEvaluationTable=new SoftwareDocEvaluationTable();
        docEvaluationTable.set软件名称("软件评估");

        softwareTest.setDocEvaluationTable(docEvaluationTable);

        SoftwareDocEvaluationTableMapper mapper=new SoftwareDocEvaluationTableMapperImpl();

        DocEvaluationTableDto docEvaluationTableDto=mapper.toDto(docEvaluationTable);

        String content=toJson(docEvaluationTableDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/doc-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocDocEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareDocEvaluationTable docEvaluationTable=new SoftwareDocEvaluationTable();
        docEvaluationTable.set软件名称("软件评估");

        softwareTest.setDocEvaluationTable(docEvaluationTable);

        SoftwareDocEvaluationTableMapper mapper=new SoftwareDocEvaluationTableMapperImpl();

        DocEvaluationTableDto docEvaluationTableDto=mapper.toDto(docEvaluationTable);

        String body=toJson(docEvaluationTableDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/doc-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void putDocDocEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareDocEvaluationTable docEvaluationTable=new SoftwareDocEvaluationTable();
        docEvaluationTable.set版本号("软件评估");

        softwareTest.setDocEvaluationTable(docEvaluationTable);

        SoftwareDocEvaluationTableMapper mapper=new SoftwareDocEvaluationTableMapperImpl();

        DocEvaluationTableDto docEvaluationTableDto=mapper.toDto(docEvaluationTable);

        String body=toJson(docEvaluationTableDto);

        softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT);
        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(put("/test/{id}/test-doc/doc-evaluation","123")
                        .contentType("application/json")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .content(body))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);

        mockMvc.perform(put("/test/{id}/test-doc/doc-evaluation","123")
                        .contentType("application/json")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    void getDocTestReport() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestReport testReport=new SoftwareTestReport();
        testReport.set主测人("测试报告");

        softwareTest.setTestReport(testReport);

        SoftwareTestReportMapper mapper=new SoftwareTestReportMapperImpl();

        TestReportDto testReportDto=mapper.toDto(testReport);

        String content=toJson(testReportDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/test-report","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocTestReport() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareTestReport testReport=new SoftwareTestReport();
        testReport.set主测人("测试报告");

        softwareTest.setTestReport(testReport);

        SoftwareTestReportMapper mapper=new SoftwareTestReportMapperImpl();

        TestReportDto testReportDto=mapper.toDto(testReport);

        String body=toJson(testReportDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/test-report","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getDocReportEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareReportEvaluationTable softwareReportEvaluationTable=new SoftwareReportEvaluationTable();
        softwareReportEvaluationTable.set确认意见("评估报告");

        softwareTest.setReportEvaluationTable(softwareReportEvaluationTable);

        SoftwareReportEvaluationMapper mapper=new SoftwareReportEvaluationMapperImpl();

        TestReportEvaluationTableDto testReportDto=mapper.toDto(softwareReportEvaluationTable);

        String content=toJson(testReportDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/test/report-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocReportEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareReportEvaluationTable softwareReportEvaluationTable=new SoftwareReportEvaluationTable();
        softwareReportEvaluationTable.set确认意见("评估报告");

        softwareTest.setReportEvaluationTable(softwareReportEvaluationTable);

        SoftwareReportEvaluationMapper mapper=new SoftwareReportEvaluationMapperImpl();

        TestReportEvaluationTableDto testReportDto=mapper.toDto(softwareReportEvaluationTable);

        String body=toJson(testReportDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/report-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getDocWorkEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareWorkEvaluationTable workEvaluationTable=new SoftwareWorkEvaluationTable();
        workEvaluationTable.set市场部审核意见("工作评估");

        softwareTest.setWorkEvaluationTable(workEvaluationTable);

        SoftwareWorkEvaluationTableMapper mapper=new SoftwareWorkEvaluationTableMapperImpl();

        WorkEvaluationTableDto workEvaluationTableDto=mapper.toDto(workEvaluationTable);

        String content=toJson(workEvaluationTableDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(get("/test/{id}/test-doc/test/work-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                )
                .andExpect(content().json(content))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocWorkEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SoftwareWorkEvaluationTable workEvaluationTable=new SoftwareWorkEvaluationTable();
        workEvaluationTable.set预计完成时间("工作评估");
        workEvaluationTable.set市场部审核意见("批准签发");

        softwareTest.setWorkEvaluationTable(workEvaluationTable);

        SoftwareWorkEvaluationTableMapper mapper=new SoftwareWorkEvaluationTableMapperImpl();

        WorkEvaluationTableDto workEvaluationTableDto=mapper.toDto(workEvaluationTable);

        String body=toJson(workEvaluationTableDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        mockMvc.perform(post("/test/{id}/test-doc/work-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void putDocApplyReportEvaluation() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        softwareTest.setState(SoftwareTestState.TEST_REPORT_DENIED);
        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);

        when(processInstanceQuery.singleResult())
                .thenReturn(null);

        mockMvc.perform(put("/test/{id}/apply-report-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        softwareTest.setState(SoftwareTestState.TEST_DOC_WORK_ACCEPTED);
        mockMvc.perform(put("/test/{id}/apply-report-evaluation","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json"))
                .andExpect(status().is(400));
    }

    @Test
    void uploadTestSchemeAuditTable() throws Exception {
        SoftwareTest softwareTest=new SoftwareTest();

        SchemeEvaluationTable schemeEvaluationTable=new SchemeEvaluationTable();

        schemeEvaluationTable.set确认意见("通过");

        softwareTest.setSchemeEvaluationTable(schemeEvaluationTable);

        TestSchemeAuditTableMapper mapper=new TestSchemeAuditTableMapperImpl();

        TestSchemeAuditTableDto workEvaluationTableDto=mapper.toDto(schemeEvaluationTable);

        String body=toJson(workEvaluationTableDto);

        when(softwareTestRepository.findByDelegationId(Mockito.anyString()))
                .thenReturn(softwareTest);
        when(delegationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(new Delegation()));

        mockMvc.perform(post("/test/{id}/audit-scheme","123")
                        .header("usrId","")
                        .header("usrName","")
                        .header("usrRole","")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getTestSchemeAuditTable() {
    }

    @Test
    void getContractTest(){
        RestTemplate restTemplate=new RestTemplate();
    }


}