package com.micro.delegationserver.model;

//除错误状态外，其余状态编码方式为大阶段（如上传、审核）+ 状态转移负责人 + 细节
public enum DelegationState {
    ERROR,//错误状态
    UPLOAD_FUNCTION_TABLE,//已经填写申请表，等待功能表
    UPLOAD_FILES,//已经填写功能表，等待文件
    AUDIT_TEST_DPARTMENT,//已经上传文件，等待测试部审核
    AUDIT_TEST_DEPARTMENT_DENIED,//测试部拒绝S
    AUDIT_MARKET_DPARTMENT,//测试部审核通过，等待市场部审核
    AUDIT_MARKET_DPARTMENT_DENIED,//市场部拒绝
    AUDIT_MARKET_DPARTMENT_FURTHER,//市场部进一步审理
    QUOTATION_MARKET,//市场部审理通过，等待议价
    QUOTATION_USER,//用户评估议价
    QUOTATION_USER_DENIED,//用户拒绝价格
    QUOTATION_USER_APPLICATION,//用户申请再次议价
    TEST_MARKET_APPLICATION,//用户接受议价，等待市场部完成测试申请表
    TEST_MARKET_CONTRACT,//市场部完成测试申请表，等待市场部完成合同
    IN_REVIEW,
    DENIED,
    ACCEPTED,
    COMPLETED
}
