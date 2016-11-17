package com.hmbnet.jwtdemo.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

    private static final long serialVersionUID = -3456433974355641389L;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities = null;
    boolean isCredExpired = false;

    public JwtUserDetails(String user, String pass, boolean isValid, Collection<? extends GrantedAuthority> auths) {
        username = user;
        password = pass;
        isCredExpired = !isValid;
        authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
