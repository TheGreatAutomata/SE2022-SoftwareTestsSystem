package com.micro.delegationserver.service.update.functionTable;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UpdateFunctionTableResult {
    boolean result=true;
    HttpStatus httpStatus;


    public UpdateFunctionTableResult(){

    }
    public UpdateFunctionTableResult(boolean result){
        this.result=result;
    }
}
