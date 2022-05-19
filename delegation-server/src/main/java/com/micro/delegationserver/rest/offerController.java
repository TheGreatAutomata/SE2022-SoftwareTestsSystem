package com.micro.delegationserver.rest;

import com.micro.api.OfferApi;
import com.micro.dto.OfferTableDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

public class offerController implements OfferApi {


    @Override
    public ResponseEntity<Void> createOffer(String id, String usrName, String usrId, String usrRole, OfferTableDto offerTableDto) {
        //证id与状态
        //设置参数
        //开启process

        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }
}
