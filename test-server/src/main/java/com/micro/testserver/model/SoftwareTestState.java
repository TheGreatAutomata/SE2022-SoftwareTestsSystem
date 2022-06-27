package com.micro.testserver.model;

public enum SoftwareTestState {
    AUDIT_QUALITY,//等待质量部人员审核
    AUDIT_QUALITY_DENIED,//审核不通过
    TEST_DOC_TEST_CASE,//审核通过，开始测试，等待《测试用例》
    TEST_DOC_TEST_RECORD,//等待《软件测试记录》
    TEST_DOC_BUG_LIST,//等待《软件测试问题清单》
    TEST_DOC_DOC_EVALUATION_TABLE,//等待《软件文档评审表》
    TEST_DOC_TEST_REPORT,//等待《软件测试报告》
    TEST_DOC_TEST_REPORT_EVALUATION_TABLE,//等待《测试报告检查表》
    TEST_REPORT_DENIED,//软件文档不通过,重新修改
    TEST_DOC_WORK_EVALUATION_TABLE,//等待《软件项目委托测试工作检查表》
    TEST_DOC_WORK_ACCEPTED,//被批准签发
    TEST_DOC_WORK_DENIED,//没有被批准签发
}
