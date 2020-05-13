package org.nzvirtual.api.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="refreshtokens")
public class RefreshToken {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "token")
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "created_at")
    private Date created_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void touch() {
        this.created_at = new Date();
    }

    @PrePersist
    public void prePersist() {
        this.created_at = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.created_at = new Date();
    }
}
