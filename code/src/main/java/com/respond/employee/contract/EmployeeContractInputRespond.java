package com.respond.employee.contract;

public class EmployeeContractInputRespond {
    public String name;
    public boolean isSuccess;
    public String state;
    public EmployeeContractInputRespond(String name,boolean isSuccess,String state){
        this.name = name;
        this.isSuccess = isSuccess;
        this.state = state;
    }
}
