package com.micro.testserver.rest;

import com.micro.api.AuditApi;
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
public class SoftwareAuditController implements AuditApi {

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    TestSchemeAuditTableMapper testSchemeAuditTableMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Override
    //该接口暂时停用
    public ResponseEntity<Void> uploadTestSchemeAuditTable(String id, TestSchemeAuditTableDto testSchemeAuditTableDto) {
        //该接口仅仅用于测试部人员上传评审表
        //当然，也需要判断一下委托存不存在
        Optional<Delegation> delegationOptional=delegationRepository.findById(id);
        if(delegationOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task=taskService.createTaskQuery().processDefinitionKey("test_apply").taskName("UploadTestEvaluation").processVariableValueEquals("delegationId",id).singleResult();
        if(task==null){
            //压根没开始
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SchemeEvaluationTable evaluationTable=testSchemeAuditTableMapper.toObj(testSchemeAuditTableDto);
        SoftwareTest test=runtimeService.getVariable(task.getExecutionId(),"softwareTest",SoftwareTest.class);
        test.setSchemeEvaluationTable(evaluationTable);
        test.setState(SoftwareTestState.AUDIT_QUALITY);
        softwareTestRepository.save(test);
        runtimeService.setVariable(task.getExecutionId(),"softwareTest",test);

        taskService.complete(task.getId());

        return AuditApi.super.uploadTestSchemeAuditTable(id, testSchemeAuditTableDto);
    }

    @Override
    public ResponseEntity<TestSchemeAuditTableDto> getTestSchemeAuditTable(String id) {
        return AuditApi.super.getTestSchemeAuditTable(id);
    }
}
