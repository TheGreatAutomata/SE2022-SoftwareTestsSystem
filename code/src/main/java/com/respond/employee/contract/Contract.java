package com.respond.employee.contract;

public class Contract {
    public String name;
    public String createTime;
    public String body;
    public String usrName;
    public String employeeName;
    public Contract(String name, String createTime, String usrName, String employeeName, String body){
        this.name=name;
        this.createTime=createTime;
        this.usrName=usrName;
        this.employeeName=employeeName;
        this.body=body;
    }
}
