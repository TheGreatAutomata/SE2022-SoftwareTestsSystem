package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SoftwareWorkEvaluationTable implements Serializable {
    public String 软件名称;

    public String 版本号;

    public String 申报单位;

    public String 起始时间;

    public String 预计完成时间;

    public String 主测人;

    public String 实际完成时间;

    public String 市场部审核意见;

    //tableItems的实际数量小于27个，只是最大下标是26
    public Boolean[] tableItems=new Boolean[27];
}
