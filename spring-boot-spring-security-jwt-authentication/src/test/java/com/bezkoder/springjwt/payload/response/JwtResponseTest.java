package com.bezkoder.springjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {



    static JwtResponse jwtResponse = new JwtResponse("1",123L,"test","955@gmail.com",null);
    @Test
    void getAccessToken() {
        assertEquals("1",jwtResponse.getAccessToken());
    }

    @Test
    void setAccessToken() {
        jwtResponse.setAccessToken("2");
        assertEquals("2",jwtResponse.getAccessToken());
        jwtResponse.setAccessToken("1");
    }

    @Test
    void getTokenType() {
        assertEquals("Bearer", jwtResponse.getTokenType());
    }

    @Test
    void setTokenType() {
        jwtResponse.setTokenType("Bear");
        assertNotEquals("Bearer", jwtResponse.getTokenType());
        jwtResponse.setTokenType("Bearer");
    }

    @Test
    void getId() {
        assertEquals(123L,jwtResponse.getId());
    }

    @Test
    void setId() {
        jwtResponse.setId(12L);
        assertNotEquals(123L,jwtResponse.getId());
        jwtResponse.setId(123L);
    }

    @Test
    void getEmail() {
        assertEquals("955@gmail.com",jwtResponse.getEmail());
    }

    @Test
    void setEmail() {
        jwtResponse.setEmail("996@qq.com");
        assertNotEquals("955@gmail.com",jwtResponse.getEmail());
        jwtResponse.setEmail("955@gmail.com");
    }

    @Test
    void getUsername() {

        assertEquals("test",jwtResponse.getUsername());
    }

    @Test
    void setUsername() {
        jwtResponse.setUsername("nottest");
        assertNotEquals("test",jwtResponse.getUsername());
        jwtResponse.setUsername("test");

    }

    @Test
    void getRoles() {
        assertNull(jwtResponse.getRoles());
    }
}