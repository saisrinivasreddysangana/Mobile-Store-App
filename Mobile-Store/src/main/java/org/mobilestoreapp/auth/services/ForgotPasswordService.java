// ForgotPasswordService.java
package org.mobilestoreapp.auth.services;

import org.mobilestoreapp.auth.entities.ForgotPassword;
import org.mobilestoreapp.auth.repositories.ForgotPasswordRepository;
import org.mobilestoreapp.auth.repositories.UserRepository;
import org.mobilestoreapp.commons.email.EmailService;
import org.mobilestoreapp.commons.email.OtpGenerator;
import org.mobilestoreapp.commons.ratelimit.RateLimiterService;
import org.mobilestoreapp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
public class ForgotPasswordService {

    private static final Duration OTP_TTL = Duration.ofMinutes(10);
    private static final int REQUEST_LIMIT = 3;
    private static final int VERIFY_LIMIT = 5;
    private static final long WINDOW_SECONDS = 3600;

    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    private final RateLimiterService rateLimiterService;

    public ForgotPasswordService(UserRepository userRepository,
                                 ForgotPasswordRepository forgotPasswordRepository,
                                 OtpGenerator otpGenerator,
                                 EmailService emailService,
                                 RateLimiterService rateLimiterService) {
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.otpGenerator = otpGenerator;
        this.emailService = emailService;
        this.rateLimiterService = rateLimiterService;
    }

    @Transactional
    public void requestOtp(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for email"));

        String reqKey = "fp:req:" + email;
        if (!rateLimiterService.allow(reqKey, REQUEST_LIMIT, WINDOW_SECONDS)) {
            throw new IllegalStateException("Too many OTP requests, please try later");
        }

        int otp = otpGenerator.sixDigit();
        Instant now = Instant.now();
        Instant expiry = now.plus(OTP_TTL);

        var fp = forgotPasswordRepository.findByUser(user)
                .orElse(ForgotPassword.builder()
                        .user(user)
                        .otp(otp)
                        .expirationTime(expiry)
                        .attempts(0)
                        .lastSentTime(now)
                        .build());

        fp.updateOtp(otp, expiry, now);
        forgotPasswordRepository.save(fp);

        String code = OtpGenerator.toCode(otp);
        emailService.send(user.getEmail(), "Password reset OTP",
                "Use this code to reset password: " + code + " (valid for " + OTP_TTL.toMinutes() + " minutes)");
    }

    @Transactional
    public void verifyOtpAndReset(String email, String code, String newRawPassword, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for email"));

        var fp = forgotPasswordRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("No OTP requested"));

        String verKey = "fp:ver:" + email;
        if (!rateLimiterService.allow(verKey, VERIFY_LIMIT, WINDOW_SECONDS)) {
            throw new IllegalStateException("Too many attempts, please try later");
        }

        if (Instant.now().isAfter(fp.getExpirationTime())) {
            forgotPasswordRepository.delete(fp);
            throw new IllegalStateException("OTP expired");
        }

        int provided = Integer.parseInt(code);
        if (provided != fp.getOtp()) {
            fp.incrementAttempts();
            forgotPasswordRepository.save(fp);
            throw new IllegalArgumentException("Invalid OTP");
        }

        // Success: reset password and cleanup
        user.setPassword(encoder.encode(newRawPassword));
        userRepository.save(user);
        forgotPasswordRepository.delete(fp);
    }
}
