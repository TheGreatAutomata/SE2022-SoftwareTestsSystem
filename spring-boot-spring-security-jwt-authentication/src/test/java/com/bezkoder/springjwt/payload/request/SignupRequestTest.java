package com.bezkoder.springjwt.payload.request;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    SignupRequest signupRequest;
    @BeforeEach
    void init(){
        signupRequest = new SignupRequest();
        signupRequest.setEmail("955@gmail.com");
        signupRequest.setUsername("test");
        signupRequest.setPassword("12345678");
        signupRequest.setRole(null);
    }
    @Test
    void getUsername() {
        assertEquals("test", signupRequest.getUsername());

    }

    @Test
    void setUsername() {
        signupRequest.setUsername("nottest");
        assertEquals("nottest", signupRequest.getUsername());
    }

    @Test
    void getEmail() {
        assertEquals("955@gmail.com", signupRequest.getEmail());
    }

    @Test
    void setEmail() {
        signupRequest.setEmail("996@gmail.com");
        assertEquals("996@gmail.com", signupRequest.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("12345678",signupRequest.getPassword());
    }

    @Test
    void setPassword() {
        signupRequest.setPassword("no12345678");
        assertEquals("no12345678",signupRequest.getPassword());
    }

    @Test
    void getRole() {
        assertNull(signupRequest.getRole());
    }

    @Test
    void setRole() {
    }
}