package com.example.employee.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.employee.model.RefreshToken;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.service.RefreshTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;


    public OAuth2LoginSuccessHandler(UserRepository userRepository, CustomUserDetailsService customUserDetailsService, JwtService jwtService,RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email=oidcUser.getEmail();

        String providerId=oidcUser.getSubject();

      User user = userRepository
    .findByProviderAndProviderId("GOOGLE", providerId)
    .orElseGet(() -> {

        User newUser = new User();

        newUser.setUsername(email);
        newUser.setPassword(null);
        newUser.setRole("ROLE_USER");
        newUser.setProvider("GOOGLE");
        newUser.setProviderId(providerId);

        return userRepository.save(newUser);
    });
      
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());


        String token=jwtService.generateToken(userDetails);

        RefreshToken refreshToken=refreshTokenService.createRefreshToken(user);

    

       response.setContentType("application/json");

      response.getWriter().write(
    "{\"accessToken\":\"" + token +
    "\",\"refreshToken\":\"" + refreshToken.getToken() + "\"}"
    );
;

    }

}
