package com.colour.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test/security")
public class LoginController {

    @GetMapping("sessionInfo")
    public Authentication getSessionInfo() {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
//        MemberSecurityDto principal = (MemberSecurityDto) authentication.getPrincipal();
        return authentication;

    }

}
