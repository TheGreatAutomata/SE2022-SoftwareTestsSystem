package com.micro.gatewayserver.permission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EROLETest {

    @Test
    void canAccess() {
        assertTrue(EROLE.ROLE_ADMIN.canAccess(EROLE.ROLE_MODQLTY));
    }

    @Test
    void values() {
        assertNotNull(EROLE.values());
    }

    @Test
    void valueOf() {
        assertNotNull(EROLE.valueOf("ROLE_ADMIN"));
    }
}