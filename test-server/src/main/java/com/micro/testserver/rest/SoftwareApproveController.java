/*package com.micro.testserver.rest;

import com.micro.api.TestApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.dto.TestSchemeAuditTableDto;
import com.micro.testserver.mapper.TestSchemeAuditTableMapper;
import com.micro.testserver.model.SchemeEvaluationTable;
import com.micro.testserver.model.SoftwareTest;
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

import java.util.Optional;

@RestController
public class SoftwareApproveController implements TestApi {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    TestSchemeAuditTableMapper testSchemeAuditTableMapper;

    @Override
    //该函数用于质量部人员填写审核结果
    public ResponseEntity<Void> uploadApproveResult(String id, TestSchemeAuditTableDto testSchemeAuditTableDto) {
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
        SoftwareTest test=runtimeService.getVariable(task.getExecutionId(),"softwareTest",SoftwareTest.class);
        test.setSchemeEvaluationTable(evaluationTable);
        if(evaluationTable.getResult().equals("ok")){
            runtimeService.setVariable(task.getExecutionId(),"auditResult",true);
            test.setState(SoftwareTestState.TEST_DOC_TEST_CASE);

        }else{
            runtimeService.setVariable(task.getExecutionId(),"auditResult",false);
            test.setState(SoftwareTestState.AUDIT_QUALITY_DENIED);
        }
        softwareTestRepository.save(test);
        runtimeService.setVariable(task.getExecutionId(),"softwareTest",test);
        taskService.complete(task.getId());
        return TestApi.super.uploadApproveResult(id, testSchemeAuditTableDto);
    }

}*/
