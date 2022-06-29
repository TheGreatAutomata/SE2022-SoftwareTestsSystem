package com.micro.delegationserver.rest;

import com.micro.api.AuditApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.DelegationAuditMarketResultDto;
import com.micro.dto.DelegationAuditTestResultDto;
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
public class auditController implements AuditApi {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @Autowired
    DelegationService delegationService;

    @Autowired
    DelegationRepository delegationRepository;

    //todo:处理没找到id的情况
    @Override
    public ResponseEntity<Void> auditDelegationByTestEmployees(String usrName, String usrId, String usrRole, String id, DelegationAuditTestResultDto delegationAuditTestResultDto) {

        Task task=taskService.createTaskQuery().taskName("Audit_Test").processVariableValueEquals("delegationId",id).singleResult();

        if(task==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String state=delegationAuditTestResultDto.get确认意见();

        boolean accepted= state.equals("可以测试");

        Map<String, Object> taskVariables = new HashMap<String, Object>();

        taskVariables.put("accepted", accepted);
        taskVariables.put("delegationId", id);

        /*Delegation currentDelegation=runtimeService.getVariable(task.getExecutionId(),"delegation",Delegation.class);

        if(accepted){
            currentDelegation.setState(DelegationState.ACCEPTED);
        }else{
            currentDelegation.setState(DelegationState.DENIED);
        }*/

        delegationService.saveDelegationAuditTestResult(id,delegationAuditTestResultDto);

        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            if(accepted) {
                delegation.setState(DelegationState.AUDIT_MARKET_APARTMENT);
            }else{
                delegation.setState(DelegationState.AUDIT_TEST_APARTMENT_DENIED);
            }
            delegationRepository.save(delegation);
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(401));
        }


        taskService.complete(task.getId(), taskVariables);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> auditDelegationByMarketEmployees(String usrName, String usrId, String usrRole, String id, DelegationAuditMarketResultDto delegationAuditMarketResultDto) {
        Task task=taskService.createTaskQuery().taskName("Audit_Market").processVariableValueEquals("delegationId",id).singleResult();

//        System.out.println(task);
//        System.out.println(runtimeService.getVariable(task.getExecutionId(),"delegationId"));

        if(task==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String result=delegationAuditMarketResultDto.getResult();

        boolean accepted= result.equals("可以测试");

        Map<String, Object> taskVariables = new HashMap<String, Object>();

        taskVariables.put("accepted", accepted);

        delegationService.saveDelegationAuditMarketResult(id,result);

        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            taskVariables.put("delegationId", delegation.getDelegationId());
            if(accepted) {
                delegation.setState(DelegationState.QUOTATION_MARKET);
//                taskVariables.put("isAgree", 1);
            }else{
                delegation.setState(DelegationState.AUDIT_MARKET_APARTMENT_DENIED);
//                taskVariables.put("isAgree", 0);
            }
            delegationRepository.save(delegation);

        }else{
            return new ResponseEntity<>(HttpStatus.valueOf(401));
        }

        taskService.complete(task.getId(), taskVariables);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
