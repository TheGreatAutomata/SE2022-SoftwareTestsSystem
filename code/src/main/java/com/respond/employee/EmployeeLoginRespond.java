package com.respond.employee;


public class EmployeeLoginRespond {
    public String name;
    public String password;
    public boolean exist;
    public EmployeeLoginRespond(String name,String password,boolean exist){
        this.name=name;
        this.password=password;
        this.exist=exist;
    }
}
