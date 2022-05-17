package com.micro.delegationserver.rest;

import com.micro.api.DelegationsApi;
import com.micro.delegationserver.mapper.DelegationItemMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.delegationserver.service.DelegationService;
import com.micro.dto.DelegationItemDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<List<DelegationItemDto>> listDelegations(String usrName, String usrId, String usrRole)
    {
        List<Delegation> delegations=delegationRepository.findAllByUsrId(usrId);
        return new ResponseEntity<>(new ArrayList<>(delegationItemMapper.toDtos(delegations)), HttpStatus.OK);

    }
}
