package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SoftwareReportEvaluationTable implements Serializable {
    public String 软件名称;
    public String 委托单位;
    //tableItems的数量小于14个，只是最大下标是13
    public Boolean[] tableItems=new Boolean[14];
    public String 检查人;
    public String 日期;
    public String 确认意见;
}
