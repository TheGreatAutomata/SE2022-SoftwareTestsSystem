package com.respond.employee;

import java.util.*;

public class EmployeeDB implements IEmployeeDB {
    private final List<Employee> _employees=new ArrayList<>();

    public EmployeeDB() {
        _employees.add(new Employee("LHK","LHK123"));
        _employees.add(new Employee("LRB","LRB123"));
        _employees.add(new Employee("ZLJ","ZLJ123"));
        _employees.add(new Employee("CNT","CNT123"));
    }

    @Override
    public boolean addEmployee(String name, String password) {
        for (Employee e : _employees
        ) {
            if (e.name.equals(name)) {
                return false;
            }
        }
        _employees.add(new Employee(name, password));
        return true;
    }

    @Override
    public boolean checkEmployee(String name, String password) {
        for (Employee e : _employees
        ) {
            if (e.name.equals(name) && e.password.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
