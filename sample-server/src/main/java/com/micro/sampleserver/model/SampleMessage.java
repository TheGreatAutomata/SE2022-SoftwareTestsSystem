package com.micro.sampleserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleMessage  implements Serializable {
    public String number;
}
