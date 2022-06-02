package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    UserRepository userRepository;

    LoginRequest loginRequest = new LoginRequest();

    @Test
    public void LoginTest(){

    }
}
