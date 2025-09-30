package org.mobilestoreapp.auth.repositories;

import org.mobilestoreapp.auth.entities.ForgotPassword;
import org.mobilestoreapp.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    Optional<ForgotPassword> findByUser(User user);
    void deleteByUser(User user);

}
