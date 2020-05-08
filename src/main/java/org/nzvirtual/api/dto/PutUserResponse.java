package org.nzvirtual.api.dto;

public class PutUserResponse {
    private String status;
    private boolean passwordChanged;

    public PutUserResponse(String status, boolean passwordChanged) {
        this.status = status;
        this.passwordChanged = passwordChanged;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}
