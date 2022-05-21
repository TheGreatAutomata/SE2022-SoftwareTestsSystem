package com.micro.delegationserver.model;

import com.micro.delegationserver.mapper.OfferTableMapper;
import com.micro.dto.OfferRequestDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
public class OfferTableUnion implements Serializable {
    public OfferTable 基本信息;
    public String 用户反馈;
    public OfferTableUnion(){

    }

    public OfferTableUnion(OfferTable table, String str)
    {
        基本信息 = table;
        用户反馈 = str;
    }
}
