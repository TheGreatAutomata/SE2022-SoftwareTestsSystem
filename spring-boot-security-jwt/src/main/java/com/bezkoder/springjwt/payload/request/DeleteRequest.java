package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;

public class DeleteRequest {
    @NotBlank
    Long UserId;

    @NotBlank
    Long ConfirmUserId;

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getConfirmUserId() {
        return ConfirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        ConfirmUserId = confirmUserId;
    }
}
