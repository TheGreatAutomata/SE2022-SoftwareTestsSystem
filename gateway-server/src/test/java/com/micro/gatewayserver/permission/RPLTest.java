package com.micro.gatewayserver.permission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RPLTest {

    RPL rpl = new RPL();


    @Test
    void getRequestPrivilege() {
        assertNotNull(rpl.getRequestPrivilege(rpl.getAcceptSample()));
    }


    @Test
    void getAuditDelegationTest() {
        assertNotNull(rpl.getAuditDelegationTest());
    }

    @Test
    void getAuditDelegationMarket() {
        assertNotNull(rpl.getAuditDelegationTest());
    }

    @Test
    void getOfferDelegation() {
        assertNotNull(rpl.getOfferDelegation());
    }

    @Test
    void getCompleteDelegation() {
        assertNotNull(rpl.getCompleteDelegation());
    }

    @Test
    void getFindDelegationByUsr() {
        assertNotNull(rpl.getFindDelegationByUsr());
    }

    @Test
    void getFindDelegationByState() {
        assertNotNull(rpl.getFindDelegationByState());
    }

    @Test
    void getFindDelegationAll() {
        assertNotNull(rpl.getFindDelegationAll());
    }

    @Test
    void getDraftPerformanceTermPartyB() {
        assertNotNull(rpl.getDraftPerformanceTermPartyB());
    }

    @Test
    void getGetPerformanceTermReplyPartyB() {
        assertNotNull(rpl.getGetPerformanceTermReplyPartyB());
    }

    @Test
    void getAddContractTablePartyB() {
        assertNotNull(rpl.getAddContractTablePartyB());
    }

    @Test
    void getDownloadUnsignedContractTablePartyB() {
        assertNotNull(rpl.getDownloadUnsignedContractTablePartyB());
    }

    @Test
    void getDownloadUnsignedNondisclosureAgreementTablePartyB() {
        assertNotNull(rpl.getDownloadUnsignedNondisclosureAgreementTablePartyB());
    }

    @Test
    void getUploadContractPartyB() {
        assertNotNull(rpl.getUploadContractPartyB());
    }

    @Test
    void getGet_post_put_TestScheme() {
        assertNotNull(rpl.getGet_post_put_TestScheme());
    }

    @Test
    void getGet_TestSchemeAuditTable() {
        assertNotNull(rpl.getGet_TestSchemeAuditTable());
    }

    @Test
    void getPost_put_TestSchemeAuditTable() {
        assertNotNull(rpl.getPost_put_TestSchemeAuditTable());
    }

    @Test
    void getGet_post_put_TestCase() {
        assertNotNull(rpl.getGet_post_put_TestCase());
    }

    @Test
    void getGet_post_put_TestRecord() {
        assertNotNull(rpl.getGet_post_put_TestRecord());
    }

    @Test
    void getGet_post_put_BugList() {
        assertNotNull(rpl.getGet_post_put_BugList());
    }

    @Test
    void getGet_post_put_DocEvaluationTable() {
        assertNotNull(rpl.getGet_post_put_DocEvaluationTable());
    }

    @Test
    void getGet_post_put_TestReport() {
        assertNotNull((rpl.getGet_post_put_TestReport()));
    }

    @Test
    void getGet_ReportEvaluationTable() {
        assertNotNull(rpl.getGet_ReportEvaluationTable());
    }

    @Test
    void getPost_put_ReportEvaluationTable() {
        assertNotNull(rpl.getPost_put_ReportEvaluationTable());
    }

    @Test
    void getPost_put_WorkEvaluationTable() {
        assertNotNull(rpl.getPost_put_WorkEvaluationTable());
    }

    @Test
    void getGet_WorkEvaluationTable() {
        assertNotNull(rpl.getGet_WorkEvaluationTable());
    }

    @Test
    void getPut_AuditRequest() {
        assertNotNull(rpl.getPut_AuditRequest());
    }

    @Test
    void getGetProjects() {
        assertNotNull(rpl.getGetProjects());
    }

    @Test
    void getGetAllProjects() {
        assertNotNull(rpl.getGetAllProjects());
    }

    @Test
    void getAcceptSample() {
        assertNotNull(rpl.getAcceptSample());
    }

    @Test
    void getGetProjectByDelegationId() {
        assertNotNull(rpl.getGetProjectByDelegationId());
    }

    @Test
    void getMarketPattern() {
        assertNotNull(rpl.getMarketPattern());
    }

    @Test
    void getTestPattern() {
        assertNotNull(rpl.getTestPattern());
    }

    @Test
    void getQLTYPattern() {
        assertNotNull(rpl.getQLTYPattern());
    }

    @Test
    void getMODPattern() {
        assertNotNull(rpl.getMODPattern());
    }
}