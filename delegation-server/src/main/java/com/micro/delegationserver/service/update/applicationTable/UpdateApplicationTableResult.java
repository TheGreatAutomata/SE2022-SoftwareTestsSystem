package com.micro.delegationserver.service.update.applicationTable;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UpdateApplicationTableResult {
    boolean result=true;
    HttpStatus httpStatus;


    public UpdateApplicationTableResult(){

    }
    public UpdateApplicationTableResult(boolean result){
        this.result=result;
    }
}
