package org.mobilestoreapp.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "forgot_password", uniqueConstraints = {
        @UniqueConstraint(name = "uk_forgot_password_user", columnNames = "user_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant expirationTime;

    @Column(nullable = false)
    private Integer attempts;

    @Column(nullable = false)
    private Instant lastSentTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_forgot_password_user"))
    @JsonIgnore
    private User user;

    public void updateOtp(Integer otp, Instant expiration, Instant sentAt) {
        this.otp = otp;
        this.expirationTime = expiration;
        this.lastSentTime = sentAt;
        this.attempts = 0; // reset on new code
    }

    public void incrementAttempts() {
        this.attempts = (this.attempts == null ? 1 : this.attempts + 1);
    }

}