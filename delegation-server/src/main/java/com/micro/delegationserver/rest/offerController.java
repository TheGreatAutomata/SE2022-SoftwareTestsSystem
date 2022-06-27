package com.micro.delegationserver.rest;

import com.micro.api.OfferApi;
import com.micro.delegationserver.mapper.OfferConfirmationMapper;
import com.micro.delegationserver.mapper.OfferTableMapper;
import com.micro.delegationserver.mapper.ProjectOfferItemMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.commonserver.model.DelegationState;
import com.micro.delegationserver.model.OfferTableUnion;
import com.micro.delegationserver.repository.DelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.OfferReplyRequestDto;
import com.micro.dto.OfferRequestDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
@RestController
public class offerController implements OfferApi {

    @Autowired
    TaskService taskService;

    @Autowired
    DelegationRepository delegationRepository;

    @Autowired
    public DelegationService delegationService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    OfferTableMapper offerTableMapper;

    @Autowired
    ProjectOfferItemMapper projectOfferItemMapper;

    @Autowired
    OfferConfirmationMapper offerConfirmationMapper;

    @Override
    public ResponseEntity<Void> updateOffer(String id, String usrName, String usrId, String usrRole, OfferRequestDto offerRequestDto)
    {
        Task task = taskService.createTaskQuery().taskName("employeeUpdateOffer").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        //设置参数
        Delegation delegation=delegation_op.get();
        Map<String, Object> variables = new HashMap<String, Object>();
        if(delegation.getState() != DelegationState.QUOTATION_USER_APPLICATION)
        {
            return ResponseEntity.status(423).build();
        }
        if(Objects.equals(offerRequestDto.get市场部态度(), "拒绝"))
        {
            delegation.setState(DelegationState.AUDIT_MARKET_APARTMENT_DENIED);
            variables.put("state",2);
        }
        else
        {
            delegation.setState(DelegationState.QUOTATION_USER);
            delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息())));
            variables.put("delegation",delegation);
            variables.put("state",1);
        }
        taskService.complete(task.getId(), variables);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> createOffer(String id, String usrName, String usrId, String usrRole, OfferRequestDto offerRequestDto) {
        //证id与状态
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        //设置参数
        Delegation delegation=delegation_op.get();
//        if(delegation.getState() != DelegationState.QUOTATION_MARKET && delegation.getState() != DelegationState.ACCEPTED)
//        {
//            return ResponseEntity.status(423).build();
//        }
        delegation.setState(DelegationState.QUOTATION_USER);
        delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息())));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation",delegation);


        //开启process
        runtimeService.startProcessInstanceByKey("delegation_offer", variables);

        return new ResponseEntity(HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Void> replyOffer(String id, String usrName, String usrId, String usrRole, OfferReplyRequestDto offerReplyRequestDto) {
        Task task=taskService.createTaskQuery().taskName("usrViewOffer").processVariableValueEquals("delegationId",id).singleResult();
        if(task == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        Delegation delegation=delegation_op.get();

        if(delegation.getState() != DelegationState.QUOTATION_USER)
        {
            return ResponseEntity.status(423).build();
        }

        delegation.getOfferTableUnion().set态度(offerReplyRequestDto.get态度());
        delegation.getOfferTableUnion().set附加信息(offerReplyRequestDto.get附加信息());
        delegation.getOfferTableUnion().set确认信息(offerConfirmationMapper.toOfferConfirmation(offerReplyRequestDto.get确认信息()));

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation",delegation);
        taskService.complete(task.getId(), variables);
        return new ResponseEntity(HttpStatus.OK);
    }
}
