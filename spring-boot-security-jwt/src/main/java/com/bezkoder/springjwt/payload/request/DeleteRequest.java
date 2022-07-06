package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;

public class DeleteRequest {
    Long id;

    Long confirmed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Long confirmed) {
        this.confirmed = confirmed;
    }
}
