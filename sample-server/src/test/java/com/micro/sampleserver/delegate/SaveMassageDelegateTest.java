package com.micro.sampleserver.delegate;

import com.micro.commonserver.service.MinioService;
import com.micro.sampleserver.SampleServerApplication;
import com.micro.sampleserver.model.Sample;
import com.micro.sampleserver.model.SampleMessage;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SaveMassageDelegate.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class SaveMassageDelegateTest {

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    private DelegateExecution delegateExecution;

    @Autowired
    private SaveMassageDelegate saveMassageDelegate;

    private Sample sample;

    @BeforeEach
    void init()
    {
        SampleMessage message = new SampleMessage();
        message.set备注("theComment");
        when(delegateExecution.getVariable("sampleId"))
                .thenReturn("sampleId");
        when(delegateExecution.getVariable("message"))
                .thenReturn(message);
        when(delegateExecution.getVariable("usrName"))
                .thenReturn("usrName");
        when(delegateExecution.getVariable("usrId"))
                .thenReturn("usrId");
        sample = new Sample();
        sample.setDelegationId("sampleId");
        sample.setUsrName("usrName");
        sample.setUsrId("usrId");
        sample.setComment(message.get备注());
    }

    @Test
    void execute() {
        saveMassageDelegate.execute(delegateExecution);
        verify(mongoTemplate,times(1)).save(sample);
        verify(delegateExecution, times(1)).setVariable("id", "sampleId");
    }
}