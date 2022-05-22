package com.micro.delegationserver.rest;

import com.micro.api.CompleteApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.dto.InlineObjectDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class completeController implements CompleteApi {

    @Autowired
    TaskService taskService;

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    @Override
    public ResponseEntity<Void> completeDelegationIdPost(String id, String usrName, String usrId, String usrRole, InlineObjectDto inlineObjectDto) {
        Task task=taskService.createTaskQuery().taskName("employeeFillTable").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        Delegation delegation=delegation_op.get();

        if(delegation.getState() != DelegationState.TEST_MARKET_APPLICATION)
        {
            return ResponseEntity.status(423).build();
        }

        Map<String, Object> variables = new HashMap<String, Object>();
        delegation.setState(DelegationState.TEST_MARKET_CONTRACT);
        delegation.setProjectId(inlineObjectDto.getId());
        //variables.put("id",inlineObjectDto.getId());
        variables.put("delegation",delegation);
        taskService.complete(task.getId(), variables);

        return new ResponseEntity(HttpStatus.OK);
    }
}
