package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository  extends JpaRepository<Professor,Long> {
    @Query("""
SELECT p FROM Professor p
WHERE LOWER(TRIM(p.prenom)) = LOWER(TRIM(:prenom))
AND LOWER(TRIM(p.nom)) = LOWER(TRIM(:nom))
""")
    Optional<Professor> findByNameNormalized(
            @Param("prenom") String prenom,
            @Param("nom") String nom
    );

    @Query("""
SELECT p FROM Professor p
LEFT JOIN FETCH p.timetable
WHERE p.userId = :userId
""")
    Optional<Professor> findByUserIdWithTimetable(@Param("userId")String userId);

    Professor findByUserId(String userId);
}
