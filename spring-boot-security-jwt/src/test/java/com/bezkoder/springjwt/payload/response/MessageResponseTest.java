package com.bezkoder.springjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {
    static MessageResponse messageResponse = new MessageResponse("123");
    @Test
    void getMessage() {
        assertEquals("123", messageResponse.getMessage());
    }

    @Test
    void setMessage() {
        messageResponse.setMessage("12");
        assertNotEquals("123", messageResponse.getMessage());
        messageResponse.setMessage("123");
    }
}