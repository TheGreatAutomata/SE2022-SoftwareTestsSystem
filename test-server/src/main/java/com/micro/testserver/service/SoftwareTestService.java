package com.micro.testserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.micro.dto.AllFilesDto;
import com.micro.dto.SingleFileDto;
import com.micro.testserver.model.SoftwareFormalTestReport;
import com.micro.testserver.model.SoftwareTest;
import com.micro.testserver.repository.SoftwareTestRepository;
import com.netflix.discovery.converters.Auto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class SoftwareTestService {

    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    RestTemplate restTemplate;

    public void saveTest(SoftwareTest test){
        test.setId(new ObjectId().toString());
        softwareTestRepository.save(test);
    }

    public List<SingleFileDto> getSupportDocs(String id){
        try {
            AllFilesDto dto = restTemplate.getForEntity("http://delegation-server/delegations/" + id + "/support-docs", AllFilesDto.class).getBody();
            return dto.getFiles();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


}
