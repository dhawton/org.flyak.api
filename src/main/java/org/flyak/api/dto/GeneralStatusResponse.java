package org.flyak.api.dto;

public class GeneralStatusResponse {
    private String status;

    public GeneralStatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
