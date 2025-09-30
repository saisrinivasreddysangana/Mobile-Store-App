package org.mobilestoreapp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordRequest {
    @Email
    @NotBlank
    public String email;
}
