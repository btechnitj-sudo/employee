package com.example.employee.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.dto.LoginRequest;
import com.example.employee.dto.LoginResponse;
import com.example.employee.dto.RegisterRequest;

import com.example.employee.service.AuthenticationService;
import com.example.employee.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;

   private final AuthenticationService authenticationService;

    public AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

      return userService.register(request);
    
    
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refreshToken(@RequestParam String refreshToken){
        return authenticationService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken){
        authenticationService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }

}
