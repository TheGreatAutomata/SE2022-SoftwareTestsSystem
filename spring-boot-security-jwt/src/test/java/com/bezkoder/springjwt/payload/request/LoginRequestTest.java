package com.bezkoder.springjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    LoginRequest loginRequest;
    @BeforeEach
    void init(){
        loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("12345678");
    }

    @Test
    void getUsername() {
        assertEquals(loginRequest.getUsername(),"test");
    }

    @Test
    void setUsername() {
        loginRequest.setUsername("nottest");
        assertEquals(loginRequest.getUsername(), "nottest");

    }

    @Test
    void getPassword() {
        assertEquals(loginRequest.getPassword(),"12345678");
    }

    @Test
    void setPassword() {
        loginRequest.setPassword("1234567");
        assertNotEquals(loginRequest.getPassword(),"12345678");
    }
}