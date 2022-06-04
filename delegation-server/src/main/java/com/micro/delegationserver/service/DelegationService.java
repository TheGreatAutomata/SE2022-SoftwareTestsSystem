package com.micro.delegationserver.service;


import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.mapper.DelegationAuditTestResultMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.delegationserver.repository.MongoDBDelegationRepository;
import com.micro.dto.CreatDelegationRequestDto;
import com.micro.delegationserver.model.minioFileItem;
import com.micro.dto.DelegationAuditTestResultDto;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.micro.commonserver.service.MinioService;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class DelegationService {
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @Autowired
//    private TaskService taskService;

    @Autowired
    MinioService minioServce;

    @Autowired
    MongoDBDelegationRepository delegationRepository;

    @Autowired
    DelegationAuditTestResultMapper delegationAuditTestResultMapper;



//    @Transactional
//    public void startApplicationProcess() {
//        runtimeService.startProcessInstanceByKey("delegationApplication");
//    }
//
//    public void storeDelegation()
//    {
//        System.out.println("Store delegation....\n");
//    }
//
//    @Transactional
//    public List<Task> getTasks(String assignee) {
//        return taskService.createTaskQuery().taskAssignee(assignee).list();
//    }


    @SneakyThrows
    public boolean creatFile(String delegationId, String fileName, MultipartFile file, String fileType)
    {
        if (!Objects.equals(file, null) && !file.isEmpty()) {

            Optional<Bucket> delegationBucket = minioServce.getBucket(delegationId);
            if (delegationBucket.isEmpty()) {
                minioServce.createBucket(delegationId);
                //delegationBucket = minioServce.getBucket(delegationId);
            }
            StatObjectResponse objectStat = null;
            try {
                objectStat = minioServce.getObjectInfo(delegationId, fileName);
                return false;
            } catch (Exception e) {
                minioServce.putObject(delegationId, fileName, file);
                Map<String, String> mp = new HashMap<>();
                mp.put("fileType", fileType);
                //mp.put("User", "jsmith");
                minioServce.setTag(delegationId, fileName, mp);
            }
        }
        return true;
    }



    ///存储测试部审理意见
    public void saveDelegationAuditTestResult(String id,DelegationAuditTestResultDto resultDto){
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setAuditTestResult(delegationAuditTestResultMapper.toObj(resultDto));
            delegationRepository.save(delegation);
        }
    }

    ///存储市场部审理意见
    public void saveDelegationAuditMarketResult(String id,String result){
        Optional<Delegation> delegation_op=delegationRepository.findById(id);
        if(delegation_op.isPresent()){
            Delegation delegation=delegation_op.get();
            delegation.setAuditMarketResult(result);
            delegationRepository.save(delegation);
        }
    }


    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    //todo:检验随机数生成算法。。。
    public Delegation constructFromRequestDto(CreatDelegationRequestDto requestDto,String usrId,String usrName){
        Delegation delegation=new Delegation(usrId,usrName,delegationApplicationTableMapper.toDelegationApplicationTable(requestDto.getApplicationTable()), DelegationState.IN_REVIEW);
        delegation.setDelegationId(new ObjectId().toString());
        return delegation;
    }

    @SneakyThrows
    public List<minioFileItem> getAllFiles(String delegationId)
    {
        Iterable<Result<Item>> allFiles = minioServce.listObjects(delegationId);
        if(allFiles == null)
        {
            return null;
        }
        List<minioFileItem> fileList = new ArrayList<>();
        for(Result<Item> f : allFiles)
        {
            fileList.add(new minioFileItem(minioServce.getTags(delegationId, f.get().objectName()).get("fileType"), f.get().objectName(), minioServce.getObjectURL(delegationId, f.get().objectName())));
        }
        return fileList;
//        Map<String, String> mp = new HashMap<>();;
//        for(Result<Item> f : allFiles)
//        {
//            mp.put(minioServce.getTags(delegationId, f.get().objectName()).get("fileType"), minioServce.getObjectURL(delegationId, f.get().objectName()));
//        }
//        return mp;
    }

}