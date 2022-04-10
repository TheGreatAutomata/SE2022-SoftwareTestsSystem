package com.respond;

import lombok.Data;

@Data
public class ReportRespond {
    private final String respond;

    public ReportRespond(String id){
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(" Not Available now!");
        respond = sb.toString();
    }

}
