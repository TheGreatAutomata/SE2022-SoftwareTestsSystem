package com.micro.delegationserver.service;

import com.micro.commonserver.service.MinioService;
import com.micro.delegationserver.mapper.DelegationAuditTestResultMapper;
import com.micro.delegationserver.repository.DelegationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DelegationServiceTestForFile {
    @Autowired
    DelegationAuditTestResultMapper delegationAuditTestResultMapper;

    @Autowired
    DelegationService delegationService;

    @MockBean
    MinioService minioServce;

    @MockBean
    DelegationRepository delegationRepository;

    @Test
    void creatFile()
    {

    }
}