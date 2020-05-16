package org.nzvirtual.api.dto;

public class ServerInfoResponse {
    private String version;

    public ServerInfoResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
