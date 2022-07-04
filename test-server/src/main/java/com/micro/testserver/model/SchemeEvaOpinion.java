package com.micro.testserver.model;

import com.sun.source.doctree.SerialDataTree;
import lombok.Data;

import java.io.Serializable;

@Data
public class SchemeEvaOpinion implements Serializable {
    public String opinion;
    public String sign;
    public String date;
}
