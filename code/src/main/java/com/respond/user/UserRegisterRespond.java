package com.respond.user;


public class UserRegisterRespond {
    public String name;
    public String password;
    public boolean exist;
    public UserRegisterRespond(String name, String password, boolean exist) {
        this.name = name;
        this.password = password;
        this.exist = exist;
    }
}