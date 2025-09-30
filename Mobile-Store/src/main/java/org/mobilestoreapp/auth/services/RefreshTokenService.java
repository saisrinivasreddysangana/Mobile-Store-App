package org.mobilestoreapp.auth.services;

import org.mobilestoreapp.auth.entities.RefreshToken;
import org.mobilestoreapp.auth.entities.User;
import org.mobilestoreapp.exception.RefreshTokenExpiredException;
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
    private final RefreshTokenRepository repo;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository repo) {
        this.userRepository = userRepository;
        this.repo = repo;
    }

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        var existing = repo.findByUser(user).orElse(null);
        String newValue = UUID.randomUUID().toString();
        Instant newExpiry = Instant.now().plusMillis(refreshTokenExpiration);

        if (existing != null) {
            existing.setRefreshToken(newValue);
            existing.setExpirationTime(newExpiry);
            return repo.save(existing); // UPDATE, not INSERT
        }
        // issue new token always (rotation) and optionally delete previous
        RefreshToken token = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expirationTime(Instant.now().plusMillis(refreshTokenExpiration))
                .user(user)
                .build();
        return repo.save(token);
    }

    public RefreshToken verifyRefreshToken(String tokenValue) {
        RefreshToken token = repo.findByRefreshToken(tokenValue)
                .orElseThrow(() -> new RefreshTokenExpiredException("Refresh token not found"));
        if (token.getExpirationTime().isBefore(Instant.now())) {
            repo.delete(token);
            throw new RefreshTokenExpiredException("Refresh token expired");
        }
        return token;

    }
}