package com.respond.employee.delegation;

public class Delegation {
    public String name;
    public String createTime;
    public DelegationState state;

    public Delegation(String name, String createTime, DelegationState state) {
        this.name = name;
        this.createTime = createTime;
        this.state = state;
    }
}
