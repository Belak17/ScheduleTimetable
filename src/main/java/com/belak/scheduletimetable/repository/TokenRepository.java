package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken,Long> {
    void delete(PasswordResetToken resetToken);

    PasswordResetToken findByToken(String token);
}
