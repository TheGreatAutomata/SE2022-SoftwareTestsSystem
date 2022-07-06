package com.bezkoder.springjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteRequestTest {
    DeleteRequest deleteRequest = new DeleteRequest();
    @BeforeEach
    void init(){
        deleteRequest.setConfirmed(123L);
        deleteRequest.setId(123L);
    }

    @Test
    void getId() {
        assertNotNull(deleteRequest.getId());
    }

    @Test
    void setId() {
        deleteRequest.setId(123L);
        assertNotNull(deleteRequest.getId());
    }

    @Test
    void getConfirmed() {
        assertNotNull(deleteRequest.getConfirmed());
    }

    @Test
    void setConfirmed() {
        deleteRequest.setConfirmed(123L);
        assertNotNull(deleteRequest.getConfirmed());
    }
}