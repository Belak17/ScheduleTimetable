package com.belak.scheduletimetable.repository.user;

import com.belak.scheduletimetable.model.ResetToken;
import com.belak.scheduletimetable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetToken,Long> {
    Optional<ResetToken> findByToken(String resetToken);
    void deleteByAppUser(User appUser);
}
