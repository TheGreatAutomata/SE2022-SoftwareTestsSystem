package com.respond.user;
import java.util.*;

public class UserDB implements IUserDB {
    private final List<User> _users = new ArrayList<>();

    public UserDB() {
    }

    @Override
    public boolean addUser(String name, String password) {
        for (User u : _users
        ) {
            if (u.name.equals(name)) {
                return false;
            }
        }
        _users.add(new User(name, password));
        return true;
    }

    @Override
    public boolean checkUserRegister(String name) {
        for (User u : _users
        ) {
            if (u.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkUserLogin(String name, String password) {
        for (User u : _users
        ) {
            if (u.name.equals(name) && u.password.equals(password)) {
                return true;
            }
        }
        return false;
    }
}