package org.flyak.api.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.flyak.api.data.misc.Ranks;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "email")
    private String email;
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "verified")
    private Boolean verified;
    @Column(name = "verification_token")
    private String verificationToken;
    @Column(name = "hours")
    private float hours;
    @Column(name = "rank")
    private Ranks rank;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "updated_at")
    private Date updated_at;

    @OneToMany(mappedBy="user")
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerification_token() {
        return verificationToken;
    }

    public void setVerification_token(String verification_token) {
        this.verificationToken = verification_token;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public Ranks getRank() {
        return rank;
    }

    public void setRank(Ranks rank) {
        this.rank = rank;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PreUpdate
    public void setLastUpdate() {  this.updated_at = new Date(); }
}

