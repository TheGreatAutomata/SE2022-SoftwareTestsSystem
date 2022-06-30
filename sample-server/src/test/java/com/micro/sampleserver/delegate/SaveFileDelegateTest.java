package com.micro.sampleserver.delegate;

import com.micro.commonserver.model.MultipartInputStreamFileResource;
import com.micro.commonserver.service.MinioService;
import com.micro.sampleserver.model.Sample;
import com.micro.sampleserver.model.SampleMessage;
import io.minio.Result;
import io.minio.messages.Item;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SaveFileDelegate.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class SaveFileDelegateTest {

    @MockBean
    MinioService minioService;

    @MockBean
    private DelegateExecution delegateExecution;

    @MockBean
    private Result<Item> singleFile;

    @MockBean
    private Item singleItem;

    @Autowired
    private SaveFileDelegate saveFileDelegate;

    private MultipartInputStreamFileResource sample;

    @BeforeEach
    void init() throws IOException {
        SampleMessage message = new SampleMessage();
        message.set备注("theComment");
        when(delegateExecution.getVariable("sampleId"))
                .thenReturn("sampleId");

        when(delegateExecution.getVariable("usrName"))
                .thenReturn("usrName");
        when(delegateExecution.getVariable("usrId"))
                .thenReturn("usrId");
        MockMultipartFile sampleOri = new MockMultipartFile(
                "样品", //文件名
                "test.jpg", //originalName 相当于上传文件在客户机上的文件名
                MediaType.TEXT_PLAIN_VALUE, //文件类型
                "Hello, World!".getBytes() //文件流
        );
        sample = new MultipartInputStreamFileResource(sampleOri.getBytes(), sampleOri.getName(), sampleOri.getSize());
    }

    @Test
    void executeForHasBucket() throws Exception {


        when(delegateExecution.getVariable("sample"))
                .thenReturn(sample);
        when(minioService.hasBucket(eq("samplesampleId")))
                .thenReturn(Boolean.TRUE);
        Iterable<Result<Item>> fileList = Arrays.asList(singleFile);
        when(minioService.listObjects(Mockito.anyString()))
                .thenReturn(fileList);
        try {
            when(singleFile.get())
                    .thenReturn(singleItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        when(singleItem.objectName())
                .thenReturn("fileName");
        when(minioService.listObjects(eq("samplesampleId")))
                .thenReturn(fileList);
        saveFileDelegate.execute(delegateExecution);
        verify(minioService, times(1)).removeObject(eq("samplesampleId"), eq("fileName"));
        verify(minioService, times(0)).createBucket(Mockito.anyString());
        verify(minioService, times(1)).putObject(eq("samplesampleId"), eq("样品"), Mockito.any());
        verify(delegateExecution, times(1)).setVariable(eq("id"), eq("sampleId"));
    }

    @Test
    void executeForNoBucket() throws Exception {
        when(delegateExecution.getVariable("sample"))
                .thenReturn(sample);
        when(minioService.hasBucket("samplesampleId"))
                .thenReturn(Boolean.FALSE);
        saveFileDelegate.execute(delegateExecution);
        verify(minioService, times(0)).removeObject(eq("samplesampleId"), eq("fileName"));
        verify(minioService, times(1)).createBucket(Mockito.anyString());
        verify(minioService, times(1)).putObject(eq("samplesampleId"), eq("样品"), Mockito.any());
        verify(delegateExecution, times(1)).setVariable(eq("id"), eq("sampleId"));
    }

    @Test
    void executeForNoBucketForSampleNull() throws Exception {
        when(delegateExecution.getVariable("sample"))
                .thenReturn(null);
        when(minioService.hasBucket(eq("samplesampleId")))
                .thenReturn(Boolean.FALSE);
        saveFileDelegate.execute(delegateExecution);
        verify(minioService, times(0)).removeObject(eq("samplesampleId"), eq("fileName"));
        verify(minioService, times(1)).createBucket(eq("samplesampleId"));
        verify(minioService, times(0)).putObject(eq("samplesampleId"), eq("样品"), Mockito.any());
        verify(delegateExecution, times(1)).setVariable(eq("id"), eq("sampleId"));
    }


}