package com.hmbnet.jwtdemo.model;

import java.io.Serializable;

public class AuthRequest implements Serializable {

    private static final long serialVersionUID = 7255635127821056574L;

    private String username = null;
    private String password = null;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
