package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.repository.MongoDBContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveContractDelegate implements JavaDelegate {

    @Autowired
    MongoDBContractRepository contractRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("Save the contract table...");

        Contract currentContract = (Contract)delegateExecution.getVariable("contract");

        contractRepository.save(currentContract); // TODO: 判断合同状态 updateservice

        System.out.println(currentContract.getContractId());

        delegateExecution.setVariable("contract", currentContract);
        delegateExecution.setVariable("contractId", currentContract.getContractId());
        delegateExecution.setVariable("delegationId", currentContract.getDelegationId());

    }

}
