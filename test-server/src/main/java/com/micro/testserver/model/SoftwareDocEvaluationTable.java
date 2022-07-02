package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SoftwareDocEvaluationTable implements Serializable {
    public String 软件名称;
    public String 版本号;
    public String 评审人;
    public String 评审完成时间;
    public DocEvaResult[] docEvaResults=new DocEvaResult[36];
    public String 检查人;
}
