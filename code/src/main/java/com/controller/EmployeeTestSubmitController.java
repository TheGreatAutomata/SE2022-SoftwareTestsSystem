package com.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.respond.employee.testSubmit.*;
import com.respond.employee.EmployeeDB;
import com.respond.employee.EmployeeLoginRespond;
import com.respond.employee.IEmployeeDB;
import com.respond.employee.delegation.DelegationDB;
import com.respond.employee.delegation.EmployeeDelegationAuditRespond;
import com.respond.employee.delegation.IDelegationDB;
import com.respond.employee.testSubmit.EmployeeTestSubmitRespond;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeTestSubmitController {

    private IEmployeeDB _employeeDB;

    public EmployeeTestSubmitController(){
        _employeeDB = new EmployeeDB();
    }

    @GetMapping("/employee/home/test/submit")
    public EmployeeTestSubmitRespond submitTests(@RequestParam(value = "name", defaultValue = "default") String name, @RequestParam(value = "password", defaultValue = "default") String password, @RequestParam(value = "usrName", defaultValue = "default") String usrName, @RequestParam(value = "body", defaultValue = "default") String body){
        if(!_employeeDB.checkEmployee(name,password)){
            return new EmployeeTestSubmitRespond(name,false,"employee error");
        }
        return new EmployeeTestSubmitRespond(name, true, "ok");
    }  
}

