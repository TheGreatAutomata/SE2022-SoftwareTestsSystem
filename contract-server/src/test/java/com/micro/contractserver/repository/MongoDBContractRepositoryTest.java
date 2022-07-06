package com.micro.contractserver.repository;

import com.micro.contractserver.model.Contract;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MongoDBContractRepositoryTest {

    @Autowired
    private MongoDBContractRepository mongoDBContractRepository;

    private Contract contract;

    @BeforeEach
    void setUp() {

        contract = new Contract();
        contract.setContractId("contractId");
        contract.setDelegationId("delegationId");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByContractId() {

    }

    @Test
    void findByDelegationId() {
    }

}