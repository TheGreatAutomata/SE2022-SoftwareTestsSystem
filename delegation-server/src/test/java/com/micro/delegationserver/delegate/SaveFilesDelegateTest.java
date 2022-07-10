package com.micro.delegationserver.delegate;

import com.micro.delegationserver.service.DelegationService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SaveFilesDelegateTest {

    @MockBean
    public DelegationService delegationService;

    String goodId;

    @MockBean
    DelegateExecution delegateExecution;

    @Autowired
    SaveFilesDelegate saveFilesDelegate;

    @Test
    void execute() {
        goodId = "theId";
        when(delegateExecution.getVariable("delegationId"))
                .thenReturn(goodId);
        String str = "666";
        byte[] file = str.getBytes(StandardCharsets.UTF_8);
        when(delegateExecution.getVariable("usrManual"))
                .thenReturn(file);
        when(delegateExecution.getVariable("installationManual"))
                .thenReturn(file);
        when(delegateExecution.getVariable("operationManual"))
                .thenReturn(file);
        when(delegateExecution.getVariable("maintenanceManual"))
                .thenReturn(file);
        when(delegateExecution.getVariable("usrManual"+"Name"))
                .thenReturn(goodId);
        when(delegateExecution.getVariable("installationManual"+"Name"))
                .thenReturn(goodId);
        when(delegateExecution.getVariable("operationManual"+"Name"))
                .thenReturn(goodId);
        when(delegateExecution.getVariable("maintenanceManual"+"Name"))
                .thenReturn(goodId);
        MultipartFile f = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), file);
        saveFilesDelegate.execute(delegateExecution);
        verify(delegationService, times(4)).creatFile(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
    }
}