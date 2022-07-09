package com.micro.gatewayserver.permission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPLTest {
    CPL cpl = new CPL();

    @Test
    void getCurPrivilege() {
        assertNotNull(cpl.getCurPrivilege("ROLE_ADMIN"));
    }
}