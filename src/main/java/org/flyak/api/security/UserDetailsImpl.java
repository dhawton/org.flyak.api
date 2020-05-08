package org.flyak.api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.flyak.api.data.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    private boolean isVerified = false;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private User user;
    private Logger log = LoggerFactory.getLogger(UserDetailsImpl.class);

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities, Boolean isVerified, User user) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isVerified = isVerified;
        this.user = user;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole()))
            .collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), authorities, user.getVerified(), user);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public User getUser() { return user; }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
