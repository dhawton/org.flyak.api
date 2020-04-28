package org.flyak.api.dto;

import javax.validation.constraints.NotEmpty;

public class UserRequest {
    @NotEmpty(message = "Email required")
    private String email;
    @NotEmpty()
    private String name;
    @NotEmpty()
    private String password;
    private String newpassword;
    private boolean genpassword;

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

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public boolean isGenpassword() {
        return genpassword;
    }

    public void setGenpassword(boolean genpassword) {
        this.genpassword = genpassword;
    }
}
