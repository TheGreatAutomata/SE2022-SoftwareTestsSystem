package com.micro.gatewayserver.permission;
import org.bouncycastle.util.test.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.*;

public class RPL {
    private final String auditDelegationTest = "/audit/delegation/test/.*";
    private final String auditDelegationMarket = "/audit/delegation/market/.*";
    private final String offerDelegation = "/offer/delegation/.*";
    private final String completeDelegation = "/complete/delegation/.*";
    private final String findDelegationByUsr = "/delegations/usrId/.*";
    private final String findDelegationByState = "/delegations/state/.*";
    private final String findDelegationAll = "/delegations/all";

    private final String draftPerformanceTermPartyB = "/contract/performanceTerm/partyB";
    private final String getPerformanceTermReplyPartyB = "/contract/[0-9A-Za-z]+/performanceTerm/partyB";
    private final String addContractTablePartyB = "/contract/[0-9A-Za-z]+/contractTable/partyB";
    private final String downloadUnsignedContractTablePartyB = "/contract/[0-9A-Za-z]+/files/unsignedContractTable";
    private final String downloadUnsignedNondisclosureAgreementTablePartyB = "/contract/[0-9A-Za-z]+/files/unsignedNondisclosureAgreementTable";
    private final String uploadContractPartyB = "/contract/[0-9A-Za-z]+/files";
    private final String get_post_put_TestScheme="/test/[0-9A-Za-z]+/test-scheme";
    private final String get_TestSchemeAuditTable="/test/[0-9A-Za-z]+/test-scheme/audit-table";
    private final String post_put_TestSchemeAuditTable="/test/[0-9A-Za-z]+/audit-scheme";
    private final String get_post_put_TestCase="/test/[0-9A-Za-z]+/test-doc/test-case";
    private final String get_post_put_TestRecord="/test/[0-9A-Za-z]+/test-doc/test-record";
    private final String get_post_put_BugList="/test/[0-9A-Za-z]+/test-doc/buglist";
    private final String get_post_put_DocEvaluationTable="/test/[0-9A-Za-z]+/test-doc/doc-evaluation";
    private final String get_post_put_TestReport="/test/[0-9A-Za-z]+/test-doc/test-report";
    private final String get_ReportEvaluationTable="/test/[0-9A-Za-z]+/test-doc/test/report-evaluation";
    private final String post_put_ReportEvaluationTable="/test/[0-9A-Za-z]+/test-doc/report-evaluation";
    private final String post_put_WorkEvaluationTable="/test/[0-9A-Za-z]+/test-doc/work-evaluation";
    private final String get_WorkEvaluationTable="/test/[0-9A-Za-z]+/test-doc/test/work-evaluation";
    private final String put_AuditRequest="/test/[0-9A-Za-z]+/apply-report-evaluation";
    private final String getProjects="/test/projects";
    private final String getAllProjects="/test/projects/all";
    private final String acceptSample="/sample/accept/.*";
    private final String getProjectByDelegationId="/test/project/[0-9A-Za-z]+";

    private final String getLatexReportByDelegationId="/test/[0-9A-Za-z]+/latexReport";

    private final HashSet<String> MarketPattern = new HashSet<>();
    private final HashSet<String> TestPattern = new HashSet<>();
    private final HashSet<String> QLTYPattern = new HashSet<>();
    private final HashSet<String> MODPattern = new HashSet<>();
    public RPL(){
        MarketPattern.add(auditDelegationMarket);
        MarketPattern.add(offerDelegation);
        MarketPattern.add(completeDelegation);
        MarketPattern.add(findDelegationByUsr);
        MarketPattern.add(findDelegationByState);
        MarketPattern.add(findDelegationAll);

        MarketPattern.add(draftPerformanceTermPartyB);
        MarketPattern.add(getPerformanceTermReplyPartyB);
        MarketPattern.add(addContractTablePartyB);
        MarketPattern.add(downloadUnsignedContractTablePartyB);
        MarketPattern.add(downloadUnsignedNondisclosureAgreementTablePartyB);
        MarketPattern.add(uploadContractPartyB);
        MarketPattern.add(post_put_WorkEvaluationTable);

        MODPattern.add(findDelegationByState);
        MODPattern.add(findDelegationByUsr);
        MODPattern.add(findDelegationAll);
        MODPattern.add(get_post_put_TestScheme);
        MODPattern.add(get_TestSchemeAuditTable);
        MODPattern.add(get_post_put_TestCase);
        MODPattern.add(get_post_put_TestRecord);
        MODPattern.add(get_post_put_BugList);
        MODPattern.add(get_post_put_DocEvaluationTable);
        MODPattern.add(get_post_put_TestReport);
        MODPattern.add(get_ReportEvaluationTable);
        MODPattern.add(get_WorkEvaluationTable);
        MODPattern.add(getAllProjects);

        TestPattern.add(auditDelegationTest);
        TestPattern.add(findDelegationByUsr);
        TestPattern.add(findDelegationByState);
        TestPattern.add(findDelegationAll);

        //TestPattern.add(acceptSample);

        TestPattern.add(put_AuditRequest);

        QLTYPattern.add(post_put_TestSchemeAuditTable);
        QLTYPattern.add(post_put_ReportEvaluationTable);

    }


    //sequence is important here
    public EROLE getRequestPrivilege(String path){
        for(String p: MODPattern){
            if (Pattern.matches(p, path)) {
                return EROLE.ROLE_MODERATOR;
            }
        }
        for(String p : MarketPattern) {
            if (Pattern.matches(p, path)) {
                return EROLE.ROLE_MODMARKET;
            }
        }
        for(String p : TestPattern) {
            if (Pattern.matches(p, path)){
                return EROLE.ROLE_MODTEST;
            }
        }
        for(String p : QLTYPattern) {
            if (Pattern.matches(p, path)){
                return EROLE.ROLE_MODQLTY;
            }
        }
        return EROLE.ROLE_USER;
    }

    public String getAuditDelegationTest() {
        return auditDelegationTest;
    }

    public String getAuditDelegationMarket() {
        return auditDelegationMarket;
    }

    public String getOfferDelegation() {
        return offerDelegation;
    }

    public String getCompleteDelegation() {
        return completeDelegation;
    }

    public String getFindDelegationByUsr() {
        return findDelegationByUsr;
    }

    public String getFindDelegationByState() {
        return findDelegationByState;
    }

    public String getFindDelegationAll() {
        return findDelegationAll;
    }

    public String getDraftPerformanceTermPartyB() {
        return draftPerformanceTermPartyB;
    }

    public String getGetPerformanceTermReplyPartyB() {
        return getPerformanceTermReplyPartyB;
    }

    public String getAddContractTablePartyB() {
        return addContractTablePartyB;
    }

    public String getDownloadUnsignedContractTablePartyB() {
        return downloadUnsignedContractTablePartyB;
    }

    public String getDownloadUnsignedNondisclosureAgreementTablePartyB() {
        return downloadUnsignedNondisclosureAgreementTablePartyB;
    }

    public String getUploadContractPartyB() {
        return uploadContractPartyB;
    }

    public String getGet_post_put_TestScheme() {
        return get_post_put_TestScheme;
    }

    public String getGet_TestSchemeAuditTable() {
        return get_TestSchemeAuditTable;
    }

    public String getPost_put_TestSchemeAuditTable() {
        return post_put_TestSchemeAuditTable;
    }

    public String getGet_post_put_TestCase() {
        return get_post_put_TestCase;
    }

    public String getGet_post_put_TestRecord() {
        return get_post_put_TestRecord;
    }

    public String getGet_post_put_BugList() {
        return get_post_put_BugList;
    }

    public String getGet_post_put_DocEvaluationTable() {
        return get_post_put_DocEvaluationTable;
    }

    public String getGet_post_put_TestReport() {
        return get_post_put_TestReport;
    }

    public String getGet_ReportEvaluationTable() {
        return get_ReportEvaluationTable;
    }

    public String getPost_put_ReportEvaluationTable() {
        return post_put_ReportEvaluationTable;
    }

    public String getPost_put_WorkEvaluationTable() {
        return post_put_WorkEvaluationTable;
    }

    public String getGet_WorkEvaluationTable() {
        return get_WorkEvaluationTable;
    }

    public String getPut_AuditRequest() {
        return put_AuditRequest;
    }

    public String getGetProjects() {
        return getProjects;
    }

    public String getGetAllProjects() {
        return getAllProjects;
    }

    public String getAcceptSample() {
        return acceptSample;
    }

    public String getGetProjectByDelegationId() {
        return getProjectByDelegationId;
    }

    public HashSet<String> getMarketPattern() {
        return MarketPattern;
    }

    public HashSet<String> getTestPattern() {
        return TestPattern;
    }

    public HashSet<String> getQLTYPattern() {
        return QLTYPattern;
    }

    public HashSet<String> getMODPattern() {
        return MODPattern;
    }
}
