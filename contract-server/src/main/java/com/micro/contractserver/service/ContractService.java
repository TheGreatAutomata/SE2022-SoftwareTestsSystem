package com.micro.contractserver.service;

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
    PerformanceTermPartyBMapper performanceTermPartyBMapper;

    public Contract constructFromPerformanceTermPartyBDto(String delegationId, PerformanceTermPartyBDto performanceTermPartyBDto) {

        PerformanceTermPartyB performanceTermPartyB = performanceTermPartyBMapper.toObj(performanceTermPartyBDto);
        Contract contract = new Contract(delegationId, new ContractTable(new ContractTableExist(performanceTermPartyB.get项目名称(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get受托方乙方(), performanceTermPartyB.get合同履行期限(), performanceTermPartyB.get整改限制次数(), performanceTermPartyB.get一次整改限制的天数()), null, null));
        contract.setContractId(new ObjectId().toString());
        return contract;

    }

    /**
     * 创建指定文件至数据库
     * @param contractId
     * @param fileName
     * @param fileType
     * @param file
     * @return boolean
     */
    @SneakyThrows
    public boolean creatFile(String contractId, String fileName, String fileType, MultipartFile file) {

        // System.out.println(fileType + ": " + fileName);
        if (!Objects.equals(file, null) && !file.isEmpty()) {

            Optional<Bucket> contractBucket = minioService.getBucket(contractId);
            if (contractBucket.isEmpty()) {
                // System.out.println("...creating a new bucket");
                minioService.createBucket(contractId);
                //contractBucket = minioService.getBucket(contractId);
            }
            StatObjectResponse objectStat = null;
            try {
                objectStat = minioService.getObjectInfo(contractId, fileName);
                return false;
            } catch (Exception e) {
                minioService.putObject(contractId, fileName, file);
                // System.out.println("...storing " + fileName + " in the bucket");
                Map<String, String> mp = new HashMap<>();
                mp.put("fileType", fileType);
                minioService.setTag(contractId, fileName, mp);
            }
        } else {
            // System.out.println("!!! file is null !!!");
        }

        return true;
    }

    /**
     * 获取未签订的测试合同文件
     * @param contractId
     * @return minioFileItem
     */
    @SneakyThrows
    public minioFileItem getUnsignedContractTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");
            // System.out.println(tag);

        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");

            if(tag.equals("Contract_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }
        }

        return null;

    }

    /**
     * 获取未签订的保密协议文件文件
     * @param contractId
     * @return minioFileItem
     */
    @SneakyThrows
    public minioFileItem getUnsignedNondisclosureAgreementTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");
            // System.out.println(tag);

        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");

            if(tag.equals("NDA_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }

        }

        return null;

    }

    /**
     * 获取已签订的测试合同文件
     * @param contractId
     * @return minioFileItem
     */
    @SneakyThrows
    public minioFileItem getSignedContractTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");
            // System.out.println(tag);

        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");

            if(tag.equals("Contract_complete_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }
        }

        return null;

    }

    /**
     * 获取已签订的保密协议文件
     * @param contractId
     * @return minioFileItem
     */
    @SneakyThrows
    public minioFileItem getSignedNondisclosureAgreementTableFile(String contractId) {

        Iterable<Result<Item>> allFiles = minioService.listObjects(contractId);
        if (allFiles == null) {
            return null;
        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");
            // System.out.println(tag);

        }

        for (Result<Item> f : allFiles) {

            String tag = minioService.getTags(contractId, f.get().objectName()).get("fileType");

            if(tag.equals("NDA_complete_" + contractId)) {
                return new minioFileItem(tag, f.get().objectName(), minioService.getObjectURL(contractId, f.get().objectName()));
            }

        }

        return null;

    }

}
