package com.micro.testserver.model.sample;

import lombok.Data;

@Data
public class SoftwareSample {
    String delegationId;

    String name;
    SampleState state;
}
