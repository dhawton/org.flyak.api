package org.nzvirtual.api.dto;

import javax.validation.constraints.NotEmpty;

public class RegisterRequest {
    @NotEmpty(message="Email is required")
    private String email;
    @NotEmpty(message="Firstname is required")
    private String firstname;
    @NotEmpty(message="Lastname is required")
    private String lastname;
    @NotEmpty(message="Password is required")
    private String password;

    public RegisterRequest(@NotEmpty(message = "Email is required") String email, @NotEmpty(message = "Firstname is required") String firstname, @NotEmpty(message = "Lastname is required") String lastname, @NotEmpty(message = "Password is required") String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
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
}
