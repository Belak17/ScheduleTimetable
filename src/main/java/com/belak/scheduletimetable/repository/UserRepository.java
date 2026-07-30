package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    boolean existsByUserIdAndEmail(String userId, String email);
}
