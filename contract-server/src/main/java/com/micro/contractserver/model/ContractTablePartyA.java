package com.micro.contractserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContractTablePartyA implements Serializable {

    public String 单位全称;
    public String 授权代表;
    public String 联系人;
    public String 通讯地址;
    public String 电话;
    public String 传真;
    public String 开户银行;
    public String 账号;
    public String 邮编;

    public ContractTablePartyA() {
    }
}
