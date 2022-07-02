package com.micro.contractserver.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class NormalResponse implements Serializable {

    public String responseInfo;

    public NormalResponse() {
    }

    public NormalResponse(String responseInfo) {
        this.responseInfo = responseInfo;
    }


}
