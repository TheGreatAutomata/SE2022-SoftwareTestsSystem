package com.micro.testserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.micro.commonserver.service.MinioService;
import com.micro.contractserver.model.minioFileItem;
import com.micro.dto.AllFilesDto;
import com.micro.dto.SingleFileDto;
import com.micro.testserver.model.SoftwareFormalTestReport;
import com.micro.testserver.model.SoftwareTest;
import com.micro.testserver.model.SoftwareTestState;
import com.micro.testserver.repository.SoftwareTestRepository;
import com.netflix.discovery.converters.Auto;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
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

    @Autowired
    MinioService minioService;

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

    public boolean checkModifiable(SoftwareTestState softwareTestState,SoftwareTestState target){
        return (softwareTestState.ordinal()>target.ordinal() && softwareTestState.ordinal()<SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE.ordinal()) || softwareTestState.equals(SoftwareTestState.TEST_REPORT_DENIED) || softwareTestState.equals(SoftwareTestState.TEST_DOC_WORK_DENIED);
    }

    @SneakyThrows
    public minioFileItem getReportFile(String projectId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(projectId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(projectId, f.get().objectName()).get("fileType");

            if(tag.equals("Report_" + projectId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(projectId, f.get().objectName()));
            }
        }

        return null;

    }

}
