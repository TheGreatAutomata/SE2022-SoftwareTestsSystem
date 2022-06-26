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