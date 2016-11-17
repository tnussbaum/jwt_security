package com.hmbnet.jwtdemo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hmbnet.jwtdemo.model.AuthRequest;
import com.hmbnet.jwtdemo.model.AuthResponse;
import com.hmbnet.jwtdemo.security.TokenUtils;
import com.hmbnet.jwtdemo.service.SecurityService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthRequest authReq) throws AuthenticationException {
        if (securityService.isValidUser(authReq.getUsername(), authReq.getPassword())) {
            // Reload password post-authentication so we can generate token
            String token = this.tokenUtils.generateToken(authReq.getUsername(),
                    securityService.getUserRoles(authReq.getUsername()));
            AuthResponse response = new AuthResponse(token);

            // Return the token
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Invalid auth request");
        }
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
        String token = request.getHeader(this.tokenHeader);
        if (this.tokenUtils.canTokenBeRefreshed(token)) {
            String refreshedToken = this.tokenUtils.refreshToken(token);
            return ResponseEntity.ok(new AuthResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body("Token not eligible for refresh");
        }
    }

}
