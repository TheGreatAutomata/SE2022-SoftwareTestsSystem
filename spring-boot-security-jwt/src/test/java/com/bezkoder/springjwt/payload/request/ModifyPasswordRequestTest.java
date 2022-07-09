package com.bezkoder.springjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifyPasswordRequestTest {
    ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest();

    @BeforeEach
    void init(){
        modifyPasswordRequest.setConfirmpassword("123L");
        modifyPasswordRequest.setUsername("admin");
        modifyPasswordRequest.setNewpassword("123L");
    }

    @Test
    void getUsername() {
        assertNotNull(modifyPasswordRequest.getUsername());
    }

    @Test
    void setUsername() {
        modifyPasswordRequest.setUsername("sad");
        assertNotNull(modifyPasswordRequest.getUsername());
    }

    @Test
    void getNewpassword() {
        assertNotNull(modifyPasswordRequest.getNewpassword());
    }

    @Test
    void setNewpassword() {
        modifyPasswordRequest.setNewpassword("123L");
        assertNotNull(modifyPasswordRequest.getNewpassword());
    }

    @Test
    void getConfirmpassword() {
        assertNotNull(modifyPasswordRequest.getConfirmpassword());
    }

    @Test
    void setConfirmpassword() {
        modifyPasswordRequest.setConfirmpassword("213L");
        assertNotNull(modifyPasswordRequest.getConfirmpassword());
    }
}