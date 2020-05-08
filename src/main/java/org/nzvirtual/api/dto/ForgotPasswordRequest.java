package org.nzvirtual.api.dto;

import javax.validation.constraints.NotEmpty;

public class ForgotPasswordRequest {
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
