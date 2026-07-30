package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.ConfirmationToken;
import com.belak.scheduletimetable.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetToken,Long> {
    Optional<ResetToken> findByToken(String resetToken);
}
