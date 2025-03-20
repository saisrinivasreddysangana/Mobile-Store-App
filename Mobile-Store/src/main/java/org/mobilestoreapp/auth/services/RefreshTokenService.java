package org.mobilestoreapp.auth.services;

import org.mobilestoreapp.auth.entities.RefreshToken;
import org.mobilestoreapp.auth.entities.User;
import org.mobilestoreapp.auth.exception.RefreshTokenExpiredException;
import org.mobilestoreapp.auth.repositories.RefreshTokenRepository;
import org.mobilestoreapp.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(username).orElse(null);
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenExpiration))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token not found!"));

        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RefreshTokenExpiredException("Refresh Token expired");
        }

        return refToken;
    }
}