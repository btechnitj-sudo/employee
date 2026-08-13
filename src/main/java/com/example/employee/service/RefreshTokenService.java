package com.example.employee.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.employee.exception.RefreshTokenException;
import com.example.employee.model.RefreshToken;
import com.example.employee.model.User;
import com.example.employee.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    public RefreshToken createRefreshToken(User user) {

     RefreshToken refreshToken = new RefreshToken();

    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setExpiryDate(Instant.now().plus(7,ChronoUnit.DAYS));
    refreshToken.setUser(user);

    return refreshTokenRepository.save(refreshToken);
    }

  public  RefreshToken verifyExpiration(RefreshToken refreshToken) {
if(refreshToken.getExpiryDate().isBefore(Instant.now())){
    refreshTokenRepository.delete(refreshToken);
    throw new RefreshTokenException("Refresh token expired. Please login again");
}
return refreshToken;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }


}

