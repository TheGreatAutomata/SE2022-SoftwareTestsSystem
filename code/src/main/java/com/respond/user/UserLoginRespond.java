package com.respond.user;


public class UserLoginRespond {
    public String name;
    public String password;
    public boolean exist;
    public UserLoginRespond(String name, String password, boolean exist) {
        this.name = name;
        this.password = password;
        this.exist = exist;
    }
}