package com.suyash.authservice.service;


import com.suyash.authservice.entity.RefreshToken;
import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.repository.RefreshTokenRepository;
import com.suyash.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .userEntity(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + "Refresh token is expired");
        }
        return refreshToken;
    }

    public Optional<RefreshToken>findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }


}
