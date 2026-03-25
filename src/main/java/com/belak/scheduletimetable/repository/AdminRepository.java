package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    public Optional<Admin> findByUserId(String userId);
}
