package com.belak.scheduletimetable.repository;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    Optional<Professor> findByUserId(String userId);

    Page<Professor> findByDepartment(Pageable pageable, Departement departement);

    Page<Professor> findByNomDepartementIgnoreCase(
            String nomDepartement,
            Pageable pageable
    );


    @Query("SELECT DISTINCT p.nomDepartement FROM Professor p WHERE p.nomDepartement IS NOT NULL ORDER BY p.nomDepartement")
    List<String> findDistinctNomDepartements();
}
