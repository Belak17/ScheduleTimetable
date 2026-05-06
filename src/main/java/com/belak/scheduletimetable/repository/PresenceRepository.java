package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceRepository extends JpaRepository<Presence,Long> {
    boolean existsBySeanceIdAndStudentId(Long id, Long id1);
    Page<Presence> findByStudentAndPresentFalse(
            Student student,
            Pageable pageable
    );

    boolean existsByStudentIdAndSeanceIdAndPresentTrue(Long id, Long id1);
}
