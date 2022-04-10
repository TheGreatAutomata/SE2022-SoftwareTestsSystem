package com.controller;

import com.respond.user.UserDB;
import com.respond.user.UserRegisterRespond;
import com.respond.user.UserLoginRespond;
import com.respond.user.IUserDB;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserLoginController {
    private IUserDB _userDB;
    public UserLoginController() {
        _userDB = new UserDB();
    }

    @GetMapping("/register/{name}/{password}")
    public UserRegisterRespond getRegister(@PathVariable String name, @PathVariable String password) {
        if(_userDB.checkUserRegister(name)) {
            return new UserRegisterRespond(name, password, true);
        }
		_userDB.addUser(name, password);
        return new UserRegisterRespond(name, password, false);
    }
	
	@GetMapping("/login/{name}/{password}")
    public UserLoginRespond getLogin(@PathVariable String name, @PathVariable String password) {
        if(_userDB.checkUserLogin(name, password)) {
            return new UserLoginRespond(name, password, true);
        }
        return new UserLoginRespond(name, password, false);
    }
}