package com.micro.delegationserver.rest;

import com.micro.api.DelegationServerApi;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class privateController implements DelegationServerApi {

    @Autowired
    private TaskService taskService;

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    public ResponseEntity<Void> sampleApplicationFinished(String id) {
        Task task = taskService.createTaskQuery().taskName("SampleApplicationFinished").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            //application not found
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegationId", id);
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setState(DelegationState.UPLOAD_SAMPLE);
            delegationRepository.save(delegation);
        }
        taskService.complete(task.getId(), variables);
        return ResponseEntity.status(200).build();
    }
}
