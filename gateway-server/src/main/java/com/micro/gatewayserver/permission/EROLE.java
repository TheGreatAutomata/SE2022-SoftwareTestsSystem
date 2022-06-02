package com.micro.gatewayserver.permission;

public enum EROLE {
    ROLE_USER(0),
    //  @Deprecated
    ROLE_MODERATOR(1),
    ROLE_MODQLTY(2),
    ROLE_MODMARKET(3),
    ROLE_MODTEST(4),
    ROLE_ADMIN(5);

    private final Integer privilege;
    EROLE(int i) {
        this.privilege = i;
    }

    public boolean canAccess(EROLE need){
        if(need.privilege == 0)
            return true;
        else if(this.privilege == 5)
            return true;
        else
            return need.privilege.equals(this.privilege);
    }
}
