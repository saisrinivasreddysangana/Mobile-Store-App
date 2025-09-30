package org.mobilestoreapp.controller;

import jakarta.validation.Valid;
import org.mobilestoreapp.auth.dto.*;
import org.mobilestoreapp.auth.entities.RefreshToken;
import org.mobilestoreapp.auth.entities.User;
import org.mobilestoreapp.auth.services.AuthService;
import org.mobilestoreapp.auth.services.ForgotPasswordService;
import org.mobilestoreapp.auth.services.JwtService;
import org.mobilestoreapp.auth.services.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final ForgotPasswordService forgotPasswordService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtService jwtService,
                          ForgotPasswordService forgotPasswordService,
                          PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.forgotPasswordService = forgotPasswordService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse res = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(
                // if DTO is a Lombok class: refreshTokenRequest.getRefreshToken()
                refreshTokenRequest.getRefreshToken()
        );
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .name(user.getName())
                .email(user.getEmail())
                .build());
    }

    @PostMapping("/forgot-password/request")
    public ResponseEntity<Void> request(@Valid @RequestBody ForgotPasswordRequest request) {
        // records: request.email(), Lombok class: request.getEmail()
        forgotPasswordService.requestOtp(request.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/forgot-password/verify")
    public ResponseEntity<PasswordResetResponse> verify(@Valid @RequestBody VerifyOtpRequest request) {

        forgotPasswordService.verifyOtpAndReset(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword(),
                passwordEncoder
        );
        return ResponseEntity.ok(new PasswordResetResponse("Password reset successful"));
    }
}
