package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.repository.MongoDBContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class DeleteContractDelegate implements JavaDelegate {

    @Autowired
    MongoDBContractRepository contractRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("Delete the contract...");

        String contractId = (String)delegateExecution.getVariable("contractId");

        contractRepository.deleteById(contractId);

    }

}