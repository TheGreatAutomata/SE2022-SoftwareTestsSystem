package com.micro.delegationserver.service;


import com.micro.delegationserver.mapper.DelegationApplicationTableMapper;
import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
import com.micro.dto.CreatDelegationRequestDto;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DelegationService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public void startApplicationProcess() {
        runtimeService.startProcessInstanceByKey("delegationApplication");
    }

    public void storeDelegation()
    {
        System.out.println("Store delegation....\n");
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    @Autowired
    DelegationApplicationTableMapper delegationApplicationTableMapper;

    //todo:检验随机数生成算法。。。
    public Delegation constructFromRequestDto(CreatDelegationRequestDto requestDto,String usrId,String usrName){
        Delegation delegation=new Delegation(usrId,usrName,delegationApplicationTableMapper.toDelegationApplicationTable(requestDto.getApplicationTable()), DelegationState.IN_REVIEW);
        delegation.setDelegationId(new ObjectId().toString());
        return delegation;
    }



}