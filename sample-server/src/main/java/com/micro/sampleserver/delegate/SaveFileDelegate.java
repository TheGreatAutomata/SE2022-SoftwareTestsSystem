package com.micro.sampleserver.delegate;

//import com.micro.commonserver.model.MultipartByteFileResource;
//import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.service.MinioService;
import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class SaveFileDelegate implements JavaDelegate {
    @Autowired
    MinioService minioService;
    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {
        MultipartInputStreamFileResource sample = (MultipartInputStreamFileResource) delegateExecution.getVariable("sample");
        String id = (String) delegateExecution.getVariable("sampleId");
        if(sample != null)
        {
            String sampleId = "sample" + id;
            minioService.createBucket(sampleId);

//            minioService.putObject(sampleId, sample.getFilename(), sample.getInputStream(), sample.getSize());
        }
        delegateExecution.setVariable("id", id);
    }
}
