package com.respond.employee;

public interface IEmployeeDB {
    boolean addEmployee(String name,String password);
    boolean checkEmployee(String name,String password);
}
