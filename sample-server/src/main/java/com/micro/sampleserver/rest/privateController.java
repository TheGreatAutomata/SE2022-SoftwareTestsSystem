package com.micro.sampleserver.rest;

import com.micro.api.PrivateApi;
import com.micro.dto.StartSampleApplicationRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

public class privateController implements PrivateApi {

    @Override
    public ResponseEntity<Void> startSampleApplication(String usrName, String usrId, String usrRole, StartSampleApplicationRequestDto startSampleApplicationRequestDto) {


        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }
}
