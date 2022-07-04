package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


//测试方案评审表
@Data
public class SchemeEvaluationTable implements Serializable {
    public String result;
    public String 软件名称;
    public String 版本号;
    public String 项目编号;
    public String 测试类别;
    public String 确认意见;

    public SchemeEvaPass[] evaPasses=new SchemeEvaPass[8];
    public SchemeEvaOpinion[] evaOpinions=new SchemeEvaOpinion[5];

    public SchemeEvaluationTable(){
        for(int i=0;i<8;i++){
            evaPasses[i]=new SchemeEvaPass();
        }
        for (int i=0;i<5;i++) {
            evaOpinions[i]=new SchemeEvaOpinion();
        }
    }
}
