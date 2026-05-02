package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceRepository extends JpaRepository<Presence,Long> {
    boolean existsBySeanceIdAndStudentId(Long id, Long id1);
}
