package com.micro.gatewayserver.permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.*;

public class RPL {
    private final String auditDelegationTest = "/audit/delegation/test/.*";
    private final String auditDelegationMarket = "/audit/delegation/market/.*";
    private final String offerDelegation = "/offer/delegation/.*";
    private final String completeDelegation = "/complete/delegation/.*";
    private final HashSet<String> MarketPattern = new HashSet<>();
    private final HashSet<String> TestPattern = new HashSet<>();
    private final HashSet<String> QLTYPattern = new HashSet<>();
    public RPL(){
        MarketPattern.add(auditDelegationMarket);
        MarketPattern.add(offerDelegation);
        MarketPattern.add(completeDelegation);
//        MarketPattern.add("/delegations/usrId/.*");
//        MarketPattern.add("/delegations/state/.*");
        TestPattern.add(auditDelegationTest);
//        TestPattern.add("/delegations/usrId/.*");
//        TestPattern.add("/delegations/state/.*");
    }


    public EROLE getRequestPrivilege(String path){
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
