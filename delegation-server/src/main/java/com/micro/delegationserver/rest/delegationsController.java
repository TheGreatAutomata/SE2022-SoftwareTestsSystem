package com.micro.delegationserver.rest;

import com.micro.api.ApiUtil;
import com.micro.api.DelegationsApi;
import com.micro.api.DelegationsApi;
import com.micro.delegationserver.mapper.DelegationItemMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.DelegationItemDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class delegationsController implements DelegationsApi {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DelegationService delegationService;

    @Autowired
    private MongoDBDelegationRepository delegationRepository;

    @Autowired
    private DelegationItemMapper delegationItemMapper;

    @Override
    public ResponseEntity<List<DelegationItemDto>> listDelegations(String usrName, String usrId, String usrRole) {
        List<Delegation> delegations = delegationRepository.findAllByUsrId(usrId);
        return new ResponseEntity<>(new ArrayList<>(delegationItemMapper.toDtos(delegations)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DelegationItemDto>> getAllDelegations(String usrName, String usrId, String usrRole, String id) {
        List<Delegation> delegations = delegationRepository.findAllByUsrId(id);
        return new ResponseEntity<>(new ArrayList<>(delegationItemMapper.toDtos(delegations)), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<List<DelegationItemDto>> getAllDelegationsByState(String usrName, String usrId, String usrRole, String id) {
        List<Delegation> delegations = delegationRepository.findAllByState(DelegationState.valueOf(id));
        return new ResponseEntity<>(new ArrayList<>(delegationItemMapper.toDtos(delegations)), HttpStatus.OK);
    }
}
