package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.dto.AbsenceOverviewDto;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepository extends JpaRepository<Presence,Long> {
    boolean existsBySeanceIdAndStudentId(Long id, Long id1);

    Page<Presence> findByStudentAndPresentFalse(
            Student student,
            Pageable pageable
    );

    boolean existsByStudentIdAndSeanceIdAndPresentTrue(Long id, Long id1);

    Optional<Presence> findBySeanceIdAndStudentId(Long studentId, Long seanceId);

    Presence findBySeanceIdAndStudentIdAndPresentFalse(Long studentId, Long seanceId);

    boolean existsByStudentIdAndSeanceIdAndPresentFalse(Long id, Long id1);
    @Query("""
    SELECT new com.belak.scheduletimetable.dto.AbsenceOverviewDto(
        s.date,
        'Absent'
    )
    FROM Presence p
    JOIN p.seance s
    WHERE p.student.id = :studentId
      AND s.coursTP.id = :coursTPId
      AND p.present = false
""")
    List<AbsenceOverviewDto> findAbsenceOverview(
            Long studentId,
            Long coursTPId
    );
}
