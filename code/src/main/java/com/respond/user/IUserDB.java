package com.respond.user;

public interface IUserDB {
    boolean addUser(String name, String password);
    boolean checkUserRegister(String name);
    boolean checkUserLogin(String name, String password);
}