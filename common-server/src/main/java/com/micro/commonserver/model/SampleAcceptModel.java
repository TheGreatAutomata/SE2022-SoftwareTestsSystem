package com.micro.commonserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

@Document("SampleAcceptModel")
@Data
public class SampleAcceptModel implements Serializable {
    @MongoId
    public String delegationId;

    public List<SampleAcceptItem> 样品列表 = new ArrayList<>();

    public String 样品状态;

    public String 来样日期;

    public String 态度;

    public List<String> getString样品列表() {
        List<String> list = new ArrayList<>();
        for(var e: this.样品列表)
            list.add(e.get样品名称());
        return list;
    }
}
