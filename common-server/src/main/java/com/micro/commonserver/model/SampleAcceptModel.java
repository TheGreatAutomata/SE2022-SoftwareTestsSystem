package com.micro.commonserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document("SampleAcceptModel")
@Data
public class SampleAcceptModel {
    @MongoId
    public String delegationId;

    public List<String> 样品列表 = null;

    public String 样品状态;

    public String 来样日期;

    public String 态度;
}
