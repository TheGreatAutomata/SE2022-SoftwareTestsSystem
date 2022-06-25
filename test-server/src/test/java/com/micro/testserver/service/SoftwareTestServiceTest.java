package com.micro.testserver.service;

import com.micro.dto.SingleFileDto;
import com.netflix.discovery.converters.Auto;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SoftwareTestServiceTest {

    @Autowired
    SoftwareTestService softwareTestService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void InterTest(){
        Assertions.assertThat(0).isNotNull();
    }

    @Test
    void saveTest() {
        Assertions.assertThat(softwareTestService).isNotNull();
    }

    @Test
    void getSupportDocs() {
        List<SingleFileDto> singleFileDtos=softwareTestService.getSupportDocs("62935af34ad9751f57fd779b");
        System.out.println(singleFileDtos);
        if(singleFileDtos!=null){
            for (SingleFileDto dto:singleFileDtos
                 ) {
                System.out.println(dto);
            }
        }
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSaveTest() {
    }

    @Test
    void testGetSupportDocs() {
    }

    @Test
    void checkModifiable() {
    }
}