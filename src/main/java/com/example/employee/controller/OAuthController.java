package com.example.employee.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;

@RestController
public class OAuthController {

    private final UserRepository userRepository;

    public OAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    @GetMapping("/jwt-test")
    public String jwtTest(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/oauth2/user")
    public String getUser(Authentication authentication) {

        OidcUser oidcUser =
                (OidcUser) authentication.getPrincipal();

        return "Email: " + oidcUser.getEmail()
                + ", Google ID: " + oidcUser.getSubject();
    }

}
