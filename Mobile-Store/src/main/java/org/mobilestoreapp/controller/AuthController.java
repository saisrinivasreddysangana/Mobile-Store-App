package org.mobilestoreapp.controller;

import org.mobilestoreapp.auth.entities.RefreshToken;
import org.mobilestoreapp.auth.entities.User;
import org.mobilestoreapp.auth.services.AuthService;
import org.mobilestoreapp.auth.services.JwtService;
import org.mobilestoreapp.auth.services.RefreshTokenService;
import org.mobilestoreapp.auth.utils.AuthResponse;
import org.mobilestoreapp.auth.utils.LoginRequest;
import org.mobilestoreapp.auth.utils.RefreshTokenRequest;
import org.mobilestoreapp.auth.utils.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private  AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
       RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
       User user = refreshToken.getUser();

       String accessToken =  jwtService.generateToken(user);

       return ResponseEntity.ok(AuthResponse.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken.getRefreshToken())
               .build());
    }
}
