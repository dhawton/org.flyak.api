package org.nzvirtual.api.dto;

public class TokenResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";

    public TokenResponse(String token, String type, String refreshToken) {
        this.token = token;
        this.type = type;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
