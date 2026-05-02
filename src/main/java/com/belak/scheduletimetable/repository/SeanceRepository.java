package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.CoursTP;
import com.belak.scheduletimetable.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SeanceRepository extends JpaRepository<Seance,Long> {
    boolean existsByCoursTPAndDate(CoursTP cours, LocalDate now);

    Optional<Seance> findByCoursTPIdAndDate(Long coursTpId, LocalDate date);
}
