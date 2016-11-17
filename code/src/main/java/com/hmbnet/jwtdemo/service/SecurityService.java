package com.hmbnet.jwtdemo.service;

public interface SecurityService {

    public Boolean hasProtectedAccess();

    public boolean isValidUser(String username, String password);

    public String getUserRoles(String username);

}
