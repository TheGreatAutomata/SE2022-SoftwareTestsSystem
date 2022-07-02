package com.micro.testserver.model;

import lombok.Data;

@Data
public class TestProject {
    /**
     * 用户ID
     */
    public String usrId;
    /**
     * 用户名
     */
    public String usrName;
    /**
     * 合同ID
     */
    public String contractId;
    /**
     * 委托ID
     */
    public String delegationId;
    /**
     * 项目ID
     */
    public String projectId;


    /**
     * 项目状态
     */
    public SoftwareTestState state;

    public TestProject(){

    }
    public TestProject(String usrId,String usrName,String delegationId,String contractId,String projectId,SoftwareTestState state){
        this.usrId=usrId;
        this.usrName=usrName;
        this.delegationId=delegationId;
        this.contractId=contractId;
        this.projectId=projectId;
        this.state=state;
    }
}