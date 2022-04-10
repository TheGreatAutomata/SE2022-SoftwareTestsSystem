package com.respond.employee.testSubmit;

public class EmployeeTestSubmitRespond {
    public String usrName;
    public String employeeName;
    public boolean isSuccess;
    public EmployeeTestSubmitRespond(String usrName, boolean isSuccess, String employeeName)
    {
        this.employeeName = employeeName;
        this.usrName = usrName;
        this.isSuccess = isSuccess;
    }
}
