package com.micro.contractserver.delegate;

import com.google.common.collect.Lists;
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

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("Save the signed contract files...");

        String contractId = (String) delegateExecution.getVariable("contractId");

//        List<MultipartFile> files = (List<MultipartFile>) delegateExecution.getVariable("files");
//        List<String> filesName = Lists.newArrayList("file1", "file2", "file3", "file4");
//        for(int i=0; i<files.size(); ++i)
//        {
//            delegationService.creatFile(delegationId, filesName.get(i), files.get(i));
//        }
        //MultipartFile file = (MultipartFile) delegateExecution.getVariable("files");
        //delegationService.creatFile(delegationId, "fileTemp", file);
        //MultipartFile f = (MultipartFile) delegateExecution.getVariable("file1");
        List<String> filesName = Lists.newArrayList("Contract_complete_" + contractId, "NDA_complete_" + contractId);
        //List<String> storeName = Lists.newArrayList("file1", "file2", "file3", "file4");
        for(String s : filesName)
        {
            byte[] file = (byte[]) delegateExecution.getVariable(s);
            if(file != null)
            {
                MultipartFile f = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), file);

                contractService.creatFile(contractId, (String) delegateExecution.getVariable(s+"Name"), s, f);
            }

        }


        //delegationService.creatFile(delegationId, "file2", (MultipartFile) delegateExecution.getVariable("file1"));
        //delegationService.creatFile(delegationId, "file3", (MultipartFile) delegateExecution.getVariable("file1"));
        //delegationService.creatFile(delegationId, "file4", (MultipartFile) delegateExecution.getVariable("file1"));
        delegateExecution.setVariable("contractId", contractId);
    }
}