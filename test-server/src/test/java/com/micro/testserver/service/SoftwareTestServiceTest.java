package com.micro.testserver.service;

import com.micro.dto.SingleFileDto;
import com.netflix.discovery.converters.Auto;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SoftwareTestServiceTest {

    @Autowired
    SoftwareTestService softwareTestService;

    @Test
    void saveTest() {
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
}