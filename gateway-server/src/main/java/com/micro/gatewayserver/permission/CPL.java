package com.micro.gatewayserver.permission;

import org.springframework.context.annotation.Bean;


public class CPL {
    public EROLE getCurPrivilege(String role){
        if(role.equals("ROLE_ADMIN"))
            return EROLE.ROLE_ADMIN;
        else if(role.equals("ROLE_MODERATOR")){
            return EROLE.ROLE_MODERATOR;
        }
        else if(role.equals("ROLE_MODTEST")){
            return EROLE.ROLE_MODTEST;
        }
        else if(role.equals("ROLE_MODMARKET")){
            return EROLE.ROLE_MODMARKET;
        }
        else if(role.equals("ROLE_MODQLTY")){
            return EROLE.ROLE_MODQLTY;
        }
        else{
            return EROLE.ROLE_USER;
        }
    }
}
