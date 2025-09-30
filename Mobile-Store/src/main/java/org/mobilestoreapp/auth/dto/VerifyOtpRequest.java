package org.mobilestoreapp.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @Email @NotBlank
    public String email;
    @NotBlank @Size(min = 6, max = 6) String otp;
    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    String newPassword;


}

