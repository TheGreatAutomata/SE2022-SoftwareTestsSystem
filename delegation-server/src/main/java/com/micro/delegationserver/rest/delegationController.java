package com.micro.delegationserver.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.micro.api.DelegationApi;
import com.micro.dto.*;

import java.net.URI;

@RestController
public class delegationController implements DelegationApi{

    @Override
    public ResponseEntity<String> creatDelegation(String usrName, String usrId, String usrRole, CreatDelegationRequestDto creatDelegationRequestDto) {
        return ResponseEntity.ok(usrId);
    }
}
