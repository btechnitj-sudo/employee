package com.example.employee.service;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.employee.dto.LoginRequest;
import com.example.employee.dto.LoginResponse;
import com.example.employee.model.RefreshToken;
import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.security.CustomUserDetails;
import com.example.employee.security.JwtService;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;



    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService,UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    //login
    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

       User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(token,refreshToken.getToken());
    }

    //refresh token

    public LoginResponse refreshToken(String refreshToken) {
RefreshToken oldRefreshToken=refreshTokenService.findByToken(refreshToken);

refreshTokenService.verifyExpiration(oldRefreshToken);

User user=oldRefreshToken.getUser();

refreshTokenService.deleteByToken(refreshToken);


String newAccessToken=jwtService.generateToken(new CustomUserDetails(user));

RefreshToken newRefreshToken=refreshTokenService.createRefreshToken(user);

return new LoginResponse(newAccessToken,newRefreshToken.getToken());
    }

    //logout

    
  public void logout(String refreshToken){
        refreshTokenService.deleteByToken(refreshToken);
    }

}
