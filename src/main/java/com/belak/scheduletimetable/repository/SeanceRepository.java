package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Presence;
import com.belak.scheduletimetable.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeanceRepository extends JpaRepository<Seance,Long> {
    boolean existsByCoursTPAndDate(CoursTP cours, LocalDate now);

    Optional<Seance> findByCoursTPIdAndDate(Long coursTpId, LocalDate date);
    @Query(
            "SELECT seance FROM Seance seance "+
    " WHERE seance.coursTP.fin < :now AND seance.absencesProcessed =  FALSE"
    )
    List<Seance> findByEndTimeBeforeAndAbsencesProcessedFalse(LocalTime now);

    List<Seance> findByCoursTP(CoursTP coursTP);
}
