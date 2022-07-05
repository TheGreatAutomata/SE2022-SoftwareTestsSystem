package com.micro.commonserver.model;

//除错误状态外，其余状态编码方式为大阶段（如上传、审核）+ 状态转移负责人 + 细节
public enum DelegationState {
    ERROR,//错误状态
    UPLOAD_FUNCTION_TABLE,//已经填写申请表，等待功能表
    UPLOAD_FILES,//已经填写功能表，等待文件

    UPLOAD_SAMPLE,//已经上传手册，等待样品

//    AUDIT_SAMPLE,//样品已经上传，等待验收
//    WAIT_PUT_SAMPLE,//样品验收失败，要put样品
    AUDIT_TEST_APARTMENT,//已经上传文件，等待测试部审核

    AUDIT_TEST_APARTMENT_DENIED,//测试部拒绝S
    AUDIT_MARKET_APARTMENT,//测试部审核通过，等待市场部审核
    AUDIT_MARKET_APARTMENT_DENIED,//市场部拒绝
    AUDIT_MARKET_APARTMENT_FURTHER,//市场部进一步审理
    QUOTATION_MARKET,//市场部审理通过，等待议价
    QUOTATION_USER,//用户评估议价
    QUOTATION_USER_DENIED,//用户拒绝价格
    QUOTATION_USER_APPLICATION,//用户申请再次议价
    TEST_MARKET_APPLICATION,//用户接受议价，等待市场部完成测试申请表
    TEST_MARKET_CONTRACT,//市场部完成测试申请表，等待市场部完成合同
    PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM, // 市场部已经拟写履行期限，等待用户回复履行期限
    PARTYB_UPDATE_PERFORMANCE_TERM, // 市场部已经修改履行期限，等待用户回复履行期限
    PARTYA_ACCEPT_PERFORMANCE_TERM, // 用户已经同意履行期限，等待市场部填写测试合同
    PARTYA_REJECT_PERFORMANCE_TERM_FOR_MODIFICATION, // 用户已经申请再议履行期限，等待市场部修改履行期限
    PARTYA_REJECT_PERFORMANCE_TERM_TO_END, // 用户已经拒绝履行期限，合同被删除，委托结束
    PARTYB_ADD_CONTRACT_TABLE, // 市场部已经填写测试合同，等待用户填写测试合同
    PARTYA_ADD_CONTRACT_TABLE, // 用户已经填写测试合同，等待市场部下载合同文件后双方线下签字，并由市场部上传已签订合同
    PARTYB_UPLOAD_SIGNED_CONTRACT, // 市场部已经上传已签订合同

    IN_REVIEW,
    DENIED,
    ACCEPTED,
    COMPLETED
}
