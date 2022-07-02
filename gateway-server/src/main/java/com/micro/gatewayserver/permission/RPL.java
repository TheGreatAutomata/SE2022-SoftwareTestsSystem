package com.micro.gatewayserver.permission;
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
    private final String downloadContractTablePartyB = "/contract/[0-9A-Za-z]+/files/contractTable";
    private final String downloadNondisclosureAgreementTablePartyB = "/contract/[0-9A-Za-z]+/files/nondisclosureAgreementTable";
    private final String uploadContractPartyB = "/contract/[0-9A-Za-z]+/files";

    private final String get_post_put_TestScheme="/test/[0-9A-Za-z]+/test-scheme";
    private final String get_TestSchemeAuditTable="/test/[0-9A-Za-z]+/test-scheme/audit-table";
    private final String post_put_TestSchemeAuditTable="/test/[0-9A-Za-z]+/audit-scheme";
    private final String get_post_put_TestCase="/test/[0-9A-Za-z]+/test-doc/test-case";
    private final String get_post_put_TestRecord="/test/[0-9A-Za-z]+/test-doc/test-record";
    private final String get_post_put_BugList="/test/[0-9A-Za-z]+/test-doc/buglist";
    private final String get_post_put_DocEvaluationTable="/test/[0-9A-Za-z]+/test-doc/doc-evaluation";
    private final String get_post_put_TestReport="/test/[0-9A-Za-z]+/test-doc/test-report";
    //private final String get_Report

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
        MarketPattern.add(downloadContractTablePartyB);
        MarketPattern.add(downloadNondisclosureAgreementTablePartyB);
        MarketPattern.add(uploadContractPartyB);


        MODPattern.add(findDelegationByState);
        MODPattern.add(findDelegationByUsr);
        MODPattern.add(findDelegationAll);

        TestPattern.add(auditDelegationTest);
        TestPattern.add(findDelegationByUsr);
        TestPattern.add(findDelegationByState);
        TestPattern.add(findDelegationAll);
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
}
