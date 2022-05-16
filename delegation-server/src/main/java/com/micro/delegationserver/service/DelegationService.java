package com.micro.delegationserver.service;


import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.Delegation;
import io.minio.StatObjectResponse;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    MinioServce minioServce;

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


    public Delegation constructFromRequest(CreatDelegationRequest request) {
        return new Delegation(request.getUsrId(), request.getUsrName(), request.getApplicationTable().getName());
    }

    @SneakyThrows
    public boolean creatFile(String delegationId, String fileName, MultipartFile file)
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
            }
        }
        return true;

    }
}