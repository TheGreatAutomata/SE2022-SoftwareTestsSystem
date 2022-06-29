package com.micro.contractserver.service;

import com.micro.contractserver.mapper.ContractTablePartyBMapper;
import com.micro.contractserver.mapper.PerformanceTermPartyBMapper;
import com.micro.contractserver.model.*;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.dto.ContractTablePartyBDto;
import com.micro.commonserver.service.MinioService;
import com.micro.dto.PerformanceTermPartyBDto;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ContractService {

    @Autowired
    MinioService minioService;

    @Autowired
    MongoDBContractRepository contractRepository;

    @Autowired
    ContractTablePartyBMapper contractTablePartyBMapper;

    @Autowired
    PerformanceTermPartyBMapper performanceTermPartyBMapper;

    public Contract constructFromPerformanceTermPartyBDto(String delegationId, PerformanceTermPartyBDto performanceTermPartyBDto) {

        PerformanceTermPartyB performanceTermPartyB = performanceTermPartyBMapper.toObj(performanceTermPartyBDto);
        Contract contract = new Contract(null, null, delegationId, new ContractTable(new ContractTableExist(performanceTermPartyB.get项目名称(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get合同履行期限(), performanceTermPartyB.get整改限制次数(), performanceTermPartyB.get一次整改限制的天数()), null, null), null, null);
        contract.setContractId(new ObjectId().toString());
        return contract;

    }

    @SneakyThrows
    public boolean creatFile(String contractId, String fileName, String fileType, MultipartFile file) {

        if (!Objects.equals(file, null) && !file.isEmpty()) {

            Optional<Bucket> contractBucket = minioService.getBucket(contractId);
            if (contractBucket.isEmpty()) {
                System.out.println("create a new bucket...");
                minioService.createBucket(contractId);
                //contractBucket = minioService.getBucket(contractId);
            }
            StatObjectResponse objectStat = null;
            try {
                objectStat = minioService.getObjectInfo(contractId, fileName);
                return false;
            } catch (Exception e) {
                minioService.putObject(contractId, fileName, file);
                System.out.println("store " + fileName + " in the bucket...");
                Map<String, String> mp = new HashMap<>();
                mp.put("fileType", fileType);
                //mp.put("User", "jsmith");
                minioService.setTag(contractId, fileName, mp);
            }
        }
        return true;
    }

    @SneakyThrows
    public minioFileItem getContractTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");

            if(tag.equals("Contract_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }
        }

        return null;

    }

    @SneakyThrows
    public minioFileItem getNondisclosureAgreementTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");
            System.out.println(tag);

            if(tag.equals("NDA_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }

        }

        return null;

    }

}
