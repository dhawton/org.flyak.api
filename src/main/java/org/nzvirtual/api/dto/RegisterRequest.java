package org.nzvirtual.api.dto;

import javax.validation.constraints.NotEmpty;

public class RegisterRequest {
    @NotEmpty(message="Email is required")
    private String email;
    @NotEmpty(message="Name is required")
    private String name;
    @NotEmpty(message="Password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
