package com.micro.delegationserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectOfferItem implements Serializable {

    public String 项目;

    public String 分项;

    public String 单价;

    public String 说明;

    public Integer 行合计;
}
