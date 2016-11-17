package com.hmbnet.jwtdemo.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 7407623381356966987L;

    private String token = null;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
