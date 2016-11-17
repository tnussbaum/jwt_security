package com.hmbnet.jwtdemo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hmbnet.jwtdemo.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService {
    private static Map<String, String> validUsers = new HashMap<>();
    private static Map<String, String> userRoles = new HashMap<>();

    static {
        validUsers.put("admin", "admin");
        validUsers.put("user", "secret");

        userRoles.put("admin", "ROLE_USER,ROLE_ADMIN");
        userRoles.put("user", "ROLE_USER");
    }

    @Override
    public Boolean hasProtectedAccess() {
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    public boolean isValidUser(String username, String password) {
        if (validUsers.containsKey(username)) {
            return validUsers.get(username).equals(password);
        }
        return false;
    }

    public String getUserRoles(String username) {
        if (userRoles.containsKey(username)) {
            return userRoles.get(username);
        }
        return null;
    }

}
