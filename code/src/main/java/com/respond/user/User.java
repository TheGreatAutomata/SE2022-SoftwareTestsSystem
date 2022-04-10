package com.respond.user;

public class User {
    public String name;
    public String password;
    public User(String name,String password){
        this.name=name;
        this.password=password;
    }
    public boolean Check(String name,String password){
        return (this.name.equals(name)) && (this.password.equals(password));
    }
}