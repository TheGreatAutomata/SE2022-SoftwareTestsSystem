package com.micro.delegationserver.rest;

import com.micro.api.OfferApi;
import com.micro.delegationserver.mapper.OfferTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.model.OfferTableUnion;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.OfferRequestDto;
import com.micro.dto.OfferTableDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.activiti.engine.RuntimeService;
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
public class offerController implements OfferApi {

    @Autowired
    TaskService taskService;

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    @Autowired
    public DelegationService delegationService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    OfferTableMapper offerTableMapper;
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
        delegation.setOfferTableUnion(new OfferTableUnion(offerTableMapper.toOfferTable(offerRequestDto.get基本信息()), offerRequestDto.get用户反馈()));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("delegation",delegation);


        //开启process
        runtimeService.startProcessInstanceByKey("delegation_offer", variables);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
