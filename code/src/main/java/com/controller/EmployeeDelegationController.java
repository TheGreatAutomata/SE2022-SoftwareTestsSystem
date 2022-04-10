package com.controller;


import com.respond.employee.EmployeeDB;
import com.respond.employee.EmployeeLoginRespond;
import com.respond.employee.IEmployeeDB;
import com.respond.employee.delegation.DelegationDB;
import com.respond.employee.delegation.EmployeeDelegationAuditRespond;
import com.respond.employee.delegation.IDelegationDB;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeDelegationController {
    private IDelegationDB _delegationDB;
    private IEmployeeDB _employeeDB;

    public EmployeeDelegationController() {
        _delegationDB = new DelegationDB();
        _employeeDB = new EmployeeDB();
    }

    @GetMapping("/employee/login/{name}/{password}")
    public EmployeeLoginRespond getLogin(@PathVariable String name, @PathVariable String password) {
        //采取极其不安全的把密码写在url里的方式
        if (_employeeDB.checkEmployee(name, password)) {
            return new EmployeeLoginRespond(name, password, true);
        }
        return new EmployeeLoginRespond(name, password, false);
    }

    @GetMapping("employee/home/delegation/audit")
    public EmployeeDelegationAuditRespond getAudit() {
        return new EmployeeDelegationAuditRespond(_delegationDB.getQueuingDelegations());
    }
}
