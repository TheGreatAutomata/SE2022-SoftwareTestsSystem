package com.controller;
import com.respond.employee.EmployeeDB;
import com.respond.employee.IEmployeeDB;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.respond.employee.contract.*;

@RestController
public class EmployeeContractController {
    private IEmployeeDB _employeeDB;
    private IContractDB _contractDB;

    public EmployeeContractController(){
        _employeeDB = new EmployeeDB();
    }

    @GetMapping("/employee/home/contract/input")
    public EmployeeContractInputRespond inputContract(@RequestParam(value = "name", defaultValue = "default") String name, @RequestParam(value = "password", defaultValue = "default") String password, @RequestParam(value = "usrName", defaultValue = "default") String usrName, @RequestParam(value = "body", defaultValue = "default") String body){
        if(!_employeeDB.checkEmployee(name,password)){
            return new EmployeeContractInputRespond(name,false,"employee error");
        }
        if(_contractDB.addContract(name, "2022/4/1", usrName, name, body))
        {
            return new EmployeeContractInputRespond(name,true,"ok");
        }else return new EmployeeContractInputRespond(name,true,"add contract error");
    }
}
