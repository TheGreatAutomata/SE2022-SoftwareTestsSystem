package com.micro.sampleserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Document("Sample")
@Data
public class Sample implements Serializable {
    @MongoId
    public String delegationId;

    public String usrName;

    public String usrId;

    public String number;
}
