package com.micro.contractserver.delegate;

import com.google.common.collect.Lists;
import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.minioFileItem;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.contractserver.service.ContractService;
import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SaveContractFilesDelegate implements JavaDelegate {


    @Autowired
    public ContractService contractService;

    @Autowired
    MongoDBContractRepository contractRepository;

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("...Saving the signed contract files");

        Contract contract = (Contract)delegateExecution.getVariable("contract");
        String contractId = (String) delegateExecution.getVariable("contractId");

        List<String> filesName = Lists.newArrayList("Contract_complete_" + contractId, "NDA_complete_" + contractId);
        for(String s : filesName)
        {
            byte[] file = (byte[]) delegateExecution.getVariable(s);
            if(file != null)
            {
                MultipartFile f = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), file);

                contractService.creatFile(contractId, (String) delegateExecution.getVariable(s + "Name"), s, f);

                if(s.equals("Contract_complete_" + contractId)) {
                    minioFileItem fileItem = contractService.getSignedContractTableFile(contractId);

                    System.out.println("...Setting signed contract file for contract");

                    contract.setSignedContractTableFile(fileItem);
                } else {
                    minioFileItem fileItem = contractService.getSignedNondisclosureAgreementTableFile(contractId);

                    System.out.println("...Setting signed non-disclosure agreement file for contract");

                    contract.setSignedNondisclosureAgreementTableFile(fileItem);
                }

            } else {

                System.out.println("!!! null file is illegal !!!");

            }

        }

        contractRepository.save(contract);

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());

    }
}