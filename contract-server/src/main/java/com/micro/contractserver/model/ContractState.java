package com.micro.contractserver.model;

public enum ContractState {
    ERROR, // 错误状态
    PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM, // 市场部已经拟写履行期限，等待用户回复履行期限
    PARTYB_UPDATE_PERFORMANCE_TERM, // 市场部已经修改履行期限，等待用户回复履行期限
    PARTYA_ACCEPT_PERFORMANCE_TERM, // 用户已经同意履行期限，等待市场部填写测试合同
    PARTYA_REJECT_PERFORMANCE_TERM_FOR_MODIFICATION, // 用户已经申请再议履行期限，等待市场部修改履行期限
    PARTYA_REJECT_PERFORMANCE_TERM_TO_END, // 用户已经拒绝履行期限，合同被删除，委托结束
    PARTYB_ADD_CONTRACT_TABLE, // 市场部已经填写测试合同，等待用户填写测试合同
    PARTYA_ADD_CONTRACT_TABLE, // 用户已经填写测试合同，等待市场部下载合同文件后双方线下签字，并由市场部上传已签订合同
    PARTYB_UPLOAD_SIGNED_CONTRACT // 市场部已经上传已签订合同
}
