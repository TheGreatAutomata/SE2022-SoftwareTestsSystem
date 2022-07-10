package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.repository.MongoDBContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveContractDelegate implements JavaDelegate {

    @Autowired
    MongoDBContractRepository contractRepository;


    /**
     * 保存合同至数据库
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {

        Contract currentContract = (Contract)delegateExecution.getVariable("contract");

        // System.out.println("...Saving the contract " + currentContract.getContractId());

        contractRepository.save(currentContract); // TODO: 判断合同状态 updateservice

        delegateExecution.setVariable("contract", currentContract);
        delegateExecution.setVariable("contractId", currentContract.getContractId());
        delegateExecution.setVariable("delegationId", currentContract.getDelegationId());

    }

}
