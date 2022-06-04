package com.micro.sampleserver.delegate;

import com.micro.dto.SampleMessageApplicationRequestDto;
import com.micro.sampleserver.model.Sample;
import com.micro.sampleserver.model.SampleMessage;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;

public class SaveMassageDelegate implements JavaDelegate {

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public void execute(DelegateExecution delegateExecution) {
        SampleMessage message = (SampleMessage) delegateExecution.getVariable("message");
        String id = (String) delegateExecution.getVariable("sampleId");
        String usrId = (String) delegateExecution.getVariable("usrId");
        String usrName = (String) delegateExecution.getVariable("usrName");
        Sample sample = new Sample();
        sample.setDelegationId(id);
        sample.setUsrName(usrName);
        sample.setUsrId(usrId);
        sample.setNumber(message.getNumber());
        mongoTemplate.save(sample);
        delegateExecution.setVariable("id", id);
    }
}
