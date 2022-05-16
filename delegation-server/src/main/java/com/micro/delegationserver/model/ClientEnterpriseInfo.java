package com.micro.delegationserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClientEnterpriseInfo implements Serializable {
    public String 电话;

    public String 传真;

    public String 地址;

    public String 邮编;

    public String 联系人;

    public String 手机;

    public String eMail;

    public String 网址;
}
