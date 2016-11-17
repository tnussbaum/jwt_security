package com.hmbnet.jwtdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("@securityService.hasProtectedAccess()")
    public ResponseEntity<?> info() {
        Map<String, String> messages = new HashMap<>();
        messages.put("msg", "Success!");
        return ResponseEntity.ok(messages);
    }

}
