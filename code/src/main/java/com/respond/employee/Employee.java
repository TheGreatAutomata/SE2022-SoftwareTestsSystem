package com.respond.employee;

public class Employee {
    public String name;
    public String password;

    public Employee(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean Check(String name, String password) {
        return (this.name.equals(name)) && (this.password.equals(password));
    }
}
