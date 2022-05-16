package com.micro.delegationserver.delegate;

import com.google.common.collect.Lists;
import com.micro.delegationserver.service.DelegationService;
import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public class SaveFilesDelegate implements JavaDelegate {

    @Autowired
    public DelegationService delegationService;

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Save the delegation files.");
        String delegationId = (String) delegateExecution.getVariable("delegationId");
//        List<MultipartFile> files = (List<MultipartFile>) delegateExecution.getVariable("files");
//        List<String> filesName = Lists.newArrayList("file1", "file2", "file3", "file4");
//        for(int i=0; i<files.size(); ++i)
//        {
//            delegationService.creatFile(delegationId, filesName.get(i), files.get(i));
//        }
        //MultipartFile file = (MultipartFile) delegateExecution.getVariable("files");
        //delegationService.creatFile(delegationId, "fileTemp", file);
        //MultipartFile f = (MultipartFile) delegateExecution.getVariable("file1");
        MultipartFile f = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), (byte[]) delegateExecution.getVariable("file1"));
        delegationService.creatFile(delegationId, "file1", f);
        //delegationService.creatFile(delegationId, "file2", (MultipartFile) delegateExecution.getVariable("file1"));
        //delegationService.creatFile(delegationId, "file3", (MultipartFile) delegateExecution.getVariable("file1"));
        //delegationService.creatFile(delegationId, "file4", (MultipartFile) delegateExecution.getVariable("file1"));
        delegateExecution.setVariable("applicationId", delegationId);
    }
}
