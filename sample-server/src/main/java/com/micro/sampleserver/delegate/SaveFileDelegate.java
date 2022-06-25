package com.micro.sampleserver.delegate;

//import com.micro.commonserver.model.MultipartByteFileResource;
//import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.service.MinioService;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class SaveFileDelegate implements JavaDelegate {
    @Autowired
    MinioService minioService;
    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {
        String id = (String) delegateExecution.getVariable("sampleId");
        MultipartInputStreamFileResource sample = (MultipartInputStreamFileResource) delegateExecution.getVariable("sample");
        if(sample != null)
        {
            String sampleId = "sample" + id;
            if(minioService.hasBucket(sampleId))
            {
                Iterable<Result<Item>> allFiles = minioService.listObjects(sampleId);
                if(allFiles != null)
                {
                    for(Result<Item> f : allFiles)
                    {
                        minioService.removeObject(sampleId, f.get().objectName());
                    }
                }
            }
            else minioService.createBucket(sampleId);
            byte[] file = (byte[]) sample.getInputStream();
            MultipartFile f = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), file);
            minioService.putObject(sampleId, sample.getFilename(), f);
//            minioService.putObject(sampleId, sample.getFilename(), sample.getInputStream(), sample.getSize());
        }
        delegateExecution.setVariable("id", id);
    }
}
