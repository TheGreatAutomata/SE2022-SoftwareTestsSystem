package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerformanceTermPartyB implements Serializable {

    public String 项目名称;
    public String 受托方乙方;
    public String 合同履行期限;
    public String 整改限制次数;
    public String 一次整改限制的天数;

    public PerformanceTermPartyB() {
    }

    public PerformanceTermPartyB(String 项目名称, String 受托方乙方, String 合同履行期限, String 整改限制次数, String 一次整改限制的天数) {

        this.项目名称 = 项目名称;
        this.受托方乙方 = 受托方乙方;
        this.合同履行期限 = 合同履行期限;
        this.整改限制次数 = 整改限制次数;
        this.一次整改限制的天数 = 一次整改限制的天数;

    }
}
