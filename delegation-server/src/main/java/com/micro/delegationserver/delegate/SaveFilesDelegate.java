package com.micro.delegationserver.delegate;

import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SaveFilesDelegate implements JavaDelegate {

    @Autowired
    public DelegationService delegationService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the delegation files.");
        String delegationId = (String) delegateExecution.getVariable("delegationId");
        List<MultipartFile> files = (List<MultipartFile>) delegateExecution.getVariable("files");
        List<String> filesName = List.of("file1", "file2", "file3", "file4");
        for(int i=0; i<files.size(); ++i)
        {
            delegationService.creatFile(delegationId, filesName.get(i), files.get(i));
        }
        delegateExecution.setVariable("applicationId", delegationId);
    }
}
