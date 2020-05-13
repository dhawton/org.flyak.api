package org.nzvirtual.api.dto;

import javax.validation.constraints.NotEmpty;

public class UserRequest {
    @NotEmpty(message = "Email required")
    private String email;
    @NotEmpty()
    private String firstname;
    @NotEmpty()
    private String lastname;
    @NotEmpty()
    private String password;
    private String newpassword;
    private boolean genpassword;

    public UserRequest() {}

    public UserRequest(@NotEmpty(message = "Email required") String email, @NotEmpty() String firstname, @NotEmpty() String lastname, @NotEmpty() String password, String newpassword, boolean genpassword) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.newpassword = newpassword;
        this.genpassword = genpassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
