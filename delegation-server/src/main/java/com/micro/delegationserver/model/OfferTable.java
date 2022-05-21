package com.micro.delegationserver.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OfferTable implements Serializable {

    public String 软件名称;

    public List<ProjectOfferItem> 项目列表 = null;

    public Integer 小计;

    public Integer 税率8percent;

    public Integer 总计;
}
